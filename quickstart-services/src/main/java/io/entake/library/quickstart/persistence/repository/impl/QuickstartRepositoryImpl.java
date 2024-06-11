package io.entake.library.quickstart.persistence.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.entake.library.quickstart.persistence.repository.QuickstartRepository;
import io.entake.library.quickstart.presentation.model.*;
import io.sdsolutions.particle.core.dozer.DozerMapperPlus;
import io.sdsolutions.particle.core.model.IdDTO;
import org.jooq.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import io.entake.library.quickstart.database.default_schema.tables.records.QuickstartSubmissionRecord;

import static io.entake.library.quickstart.database.default_schema.Tables.*;

@Repository
@Transactional
public class QuickstartRepositoryImpl implements QuickstartRepository {

    private final DSLContext dslContext;
    private final DozerMapperPlus mapper;
    private final ObjectMapper objectMapper;

    public QuickstartRepositoryImpl(DSLContext dslContext, DozerMapperPlus mapper, ObjectMapper objectMapper) {
        this.dslContext = dslContext;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public IdDTO addQuickstartSubmission(QuickstartSubmissionDTO submission) {
        try {
            QuickstartSubmissionRecord record = mapper.map(submission.getEntakeMetadata(), QuickstartSubmissionRecord.class);
            record.setSubmissionData(objectMapper.writeValueAsString(submission));
            record.setReceivedTime(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime());
            dslContext.insertInto(QUICKSTART_SUBMISSION).set(record).execute();

            return new IdDTO(record.getSubmissionId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QuickstartSubmissionDTO> getSubmissions() {
        return dslContext.selectFrom(QUICKSTART_SUBMISSION)
                .orderBy(QUICKSTART_SUBMISSION.RECEIVED_TIME.desc())
                .limit(5)
                .fetchInto(QuickstartSubmissionDTO.class);
    }

    @Override
    public QuickstartSubmissionDTO getSubmission(String submissionId) {
        return dslContext.selectFrom(QUICKSTART_SUBMISSION)
                .where(QUICKSTART_SUBMISSION.SUBMISSION_ID.equal(submissionId))
                .limit(1)
                .fetchOneInto(QuickstartSubmissionDTO.class);
    }

}
