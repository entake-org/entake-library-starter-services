package io.entake.library.heap.persistence.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.entake.library.heap.persistence.repository.HeapRepository;
import io.entake.library.heap.presentation.model.HeapApplicationDTO;
import io.entake.particle.core.dozer.DozerMapperPlus;
import io.entake.particle.core.model.IdDTO;
import org.jooq.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import io.entake.library.heap.database.default_schema.tables.records.HeapSubmissionRecord;

import static io.entake.library.heap.database.default_schema.Tables.*;

@Repository
@Transactional
public class HeapRepositoryImpl implements HeapRepository {

    private final DSLContext dslContext;
    private final DozerMapperPlus mapper;
    private final ObjectMapper objectMapper;

    public HeapRepositoryImpl(DSLContext dslContext, DozerMapperPlus mapper, ObjectMapper objectMapper) {
        this.dslContext = dslContext;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public IdDTO addHeapSubmission(HeapApplicationDTO submission) {
        try {
            HeapSubmissionRecord record = mapper.map(submission.getEntakeMetadata(), HeapSubmissionRecord.class);
            record.setSubmissionData(objectMapper.writeValueAsString(submission));
            record.setReceivedTime(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime());
            dslContext.insertInto(HEAP_SUBMISSION).set(record).execute();

            return new IdDTO(record.getSubmissionId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<HeapApplicationDTO> getSubmissions() {
        return dslContext.selectFrom(HEAP_SUBMISSION)
                .orderBy(HEAP_SUBMISSION.RECEIVED_TIME.desc())
                .limit(5)
                .fetchInto(HeapApplicationDTO.class);
    }

    @Override
    public HeapApplicationDTO getSubmission(String submissionId) {
        return dslContext.selectFrom(HEAP_SUBMISSION)
                .where(HEAP_SUBMISSION.SUBMISSION_ID.equal(submissionId))
                .limit(1)
                .fetchOneInto(HeapApplicationDTO.class);
    }

}
