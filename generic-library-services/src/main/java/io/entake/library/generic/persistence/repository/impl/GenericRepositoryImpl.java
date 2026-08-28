package io.entake.library.generic.persistence.repository.impl;

import io.entake.library.database.library.tables.records.EntakeSubmissionDataRecord;
import io.entake.library.database.library.tables.records.EntakeSubmissionDocumentRecord;
import io.entake.library.database.library.tables.records.EntakeSubmissionRecord;
import io.entake.library.generic.persistence.repository.GenericRepository;
import io.entake.library.generic.presentation.model.*;
import io.entake.particle.core.dozer.DozerMapperPlus;
import io.entake.particle.core.model.IdDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static io.entake.library.database.library.Tables.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class GenericRepositoryImpl implements GenericRepository {

    private final DozerMapperPlus mapper;
    private final DSLContext dslContext;

    @Override
    public IdDTO addSubmission(EntakeMetadataDTO metadata, String json) {
        EntakeSubmissionRecord existingRecord = dslContext.selectFrom(ENTAKE_SUBMISSION)
                .where(ENTAKE_SUBMISSION.SUBMISSION_ID.equal(metadata.getSubmissionId()))
                .fetchOne();

        if (existingRecord != null) {
            return new IdDTO(existingRecord.getSubmissionId());
        }

        metadata.setReceivedTime(getCurrentDateTime());

        if (metadata.getDecisionDate() == null) {
            metadata.setDecisionDate(metadata.getReceivedTime());
        }

        if (StringUtils.isBlank(metadata.getDecisionOwner())) {
            metadata.setDecisionOwner("SYSTEM");
        }

        dslContext.insertInto(ENTAKE_SUBMISSION).set(mapper.map(metadata, EntakeSubmissionRecord.class)).execute();

        List<EntakeSubmissionDocumentRecord> docs = mapper.mapList(metadata.getDocuments(), EntakeSubmissionDocumentRecord.class);
        docs.forEach(doc -> doc.setSubmissionId(metadata.getSubmissionId()));
        dslContext.batchInsert(docs).execute();

        EntakeSubmissionDataRecord data = new EntakeSubmissionDataRecord();
        data.setSubmissionId(metadata.getSubmissionId());
        data.setData(json);
        dslContext.insertInto(ENTAKE_SUBMISSION_DATA).set(data).execute();

        return new IdDTO(metadata.getSubmissionId());
    }

    @Override
    public PaginatedContainerDTO<EntakeSubmissionResultDTO> findSubmissions(
            Integer page, Integer pageSize, String locale, String status, String environment, String formId
    ) {
        List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotBlank(locale)) {
            conditions.add(ENTAKE_SUBMISSION.SUBMISSION_LOCALE.equal(locale));
        }

        if (StringUtils.isNotBlank(status)) {
            conditions.add(ENTAKE_SUBMISSION.SUBMISSION_STATUS.equal(status));
        }

        if (StringUtils.isNotBlank(environment)) {
            conditions.add(ENTAKE_SUBMISSION.ENVIRONMENT.equal(environment));
        }

        if (StringUtils.isNotBlank(formId)) {
            conditions.add(ENTAKE_SUBMISSION.FORM_ID.equal(formId));
        }

        return PaginatedContainerDTO.<EntakeSubmissionResultDTO>builder()
                .results(
                    dslContext.select(ArrayUtils.addAll(ENTAKE_SUBMISSION.fields(), ENTAKE_SUBMISSION_DATA.DATA))
                        .from(ENTAKE_SUBMISSION)
                        .innerJoin(ENTAKE_SUBMISSION_DATA).on(ENTAKE_SUBMISSION.SUBMISSION_ID.equal(ENTAKE_SUBMISSION_DATA.SUBMISSION_ID))
                        .where(conditions)
                        .orderBy(ENTAKE_SUBMISSION.RECEIVED_TIME.desc())
                        .limit(pageSize)
                        .offset(pageSize * page)
                        .fetchInto(EntakeSubmissionResultDTO.class)
                )
                .totalCount(dslContext.selectCount().from(ENTAKE_SUBMISSION).where(conditions).fetchOneInto(Integer.class))
                .pageNumber(page)
                .pageSize(pageSize)
                .build();
    }

    private LocalDateTime getCurrentDateTime() {
        return OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

}
