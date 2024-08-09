package io.entake.library.quickstart.persistence.repository;

import io.entake.library.quickstart.presentation.model.QuickstartSubmissionDTO;
import io.entake.particle.core.model.IdDTO;

import java.util.List;

public interface QuickstartRepository {

    IdDTO addQuickstartSubmission(QuickstartSubmissionDTO submission);

    List<QuickstartSubmissionDTO> getSubmissions();

    QuickstartSubmissionDTO getSubmission(String submissionId);

}
