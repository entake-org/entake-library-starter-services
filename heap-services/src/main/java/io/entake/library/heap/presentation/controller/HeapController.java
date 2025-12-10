package io.entake.library.heap.presentation.controller;

import io.entake.library.heap.persistence.repository.HeapRepository;
import io.entake.library.heap.presentation.model.HeapAddressDTO;
import io.entake.library.heap.presentation.model.HeapApplicationDTO;
import io.entake.library.heap.presentation.model.HeapEntrypointRequestDTO;
import io.entake.library.heap.presentation.model.HeapEntrypointResponseDTO;
import io.entake.particle.core.model.IdDTO;
import io.entake.particle.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class HeapController {

    private final HeapRepository heapRepository;

    public HeapController(HeapRepository heapRepository) {
        this.heapRepository = heapRepository;
    }

    @PostMapping("/submissions")
    public ResponseEntity<IdDTO> addHeapApplication(@RequestBody HeapApplicationDTO application) {
        IdDTO applicantId = heapRepository.addHeapSubmission(application);
        return new ResponseEntity<>(applicantId, HttpStatus.OK);
    }

    @PostMapping("/entrypoint")
    public ResponseEntity<HeapEntrypointResponseDTO> executeHeapEntrypoint(@RequestBody HeapEntrypointRequestDTO entrypointRequest) {
        return new ResponseEntity<>(
                HeapEntrypointResponseDTO.builder()
                        .firstName(entrypointRequest.getFirstName())
                        .middleName("J")
                        .lastName(entrypointRequest.getLastName())
                        .dateOfBirth(entrypointRequest.getDateOfBirth())
                        .originalCaseNumber(UUID.randomUUID().toString().replace("-", ""))
                        .homeAddress(
                                HeapAddressDTO.builder()
                                        .streetLine1("123 Main St.")
                                        .streetLine2("Apt 1")
                                        .city("New York")
                                        .state("NY")
                                        .zipcode("10001")
                                        .build()
                        ).build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/submissions")
    public List<HeapApplicationDTO> getSubmissions() {
        List<HeapApplicationDTO> dtos = heapRepository.getSubmissions();

        if (dtos.isEmpty()) {
            throw new NotFoundException();
        }

        return dtos;
    }

    @GetMapping("/submissions/{submissionId}")
    public HeapApplicationDTO getSubmission(@PathVariable String submissionId) {
        HeapApplicationDTO submission = heapRepository.getSubmission(submissionId);

        if (submission == null) {
            throw new NotFoundException();
        }

        return submission;
    }

}
