package io.entake.library.generic.persistence.repository;

import io.entake.library.generic.presentation.model.EntakeMetadataDTO;
import io.entake.library.generic.presentation.model.EntakeSubmissionResultDTO;
import io.entake.library.generic.presentation.model.PaginatedContainerDTO;
import io.entake.particle.core.model.IdDTO;

public interface GenericRepository {
    IdDTO addSubmission(EntakeMetadataDTO metadata, String json);
    PaginatedContainerDTO<EntakeSubmissionResultDTO> findSubmissions(Integer page, Integer pageNumber, String locale, String status, String environment, String formId);
}
