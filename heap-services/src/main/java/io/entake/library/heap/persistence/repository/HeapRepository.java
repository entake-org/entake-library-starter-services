package io.entake.library.heap.persistence.repository;

import io.entake.library.heap.presentation.model.HeapApplicationDTO;
import io.sdsolutions.particle.core.model.IdDTO;

import java.util.List;

public interface HeapRepository {

    IdDTO addHeapSubmission(HeapApplicationDTO submission);

    List<HeapApplicationDTO> getSubmissions();

    HeapApplicationDTO getSubmission(String submissionId);

}
