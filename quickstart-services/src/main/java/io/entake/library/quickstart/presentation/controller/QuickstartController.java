package io.entake.library.quickstart.presentation.controller;

import io.entake.library.quickstart.persistence.repository.QuickstartRepository;
import io.entake.library.quickstart.presentation.model.QuickstartSubmissionDTO;
import io.entake.particle.core.model.IdDTO;
import io.entake.particle.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuickstartController {

    private final QuickstartRepository quickstartRepository;

    public QuickstartController(QuickstartRepository quickstartRepository) {
        this.quickstartRepository = quickstartRepository;
    }

    @PostMapping("/submissions")
    public ResponseEntity<IdDTO> addSubmission(@RequestBody QuickstartSubmissionDTO registration) {
        IdDTO submissionId = quickstartRepository.addQuickstartSubmission(registration);

        return new ResponseEntity<>(submissionId, HttpStatus.OK);
    }

    @GetMapping("/submissions")
    public List<QuickstartSubmissionDTO> getSubmissions() {
        List<QuickstartSubmissionDTO> dtos = quickstartRepository.getSubmissions();

        if (dtos.isEmpty()) {
            throw new NotFoundException();
        }

        return dtos;
    }

    @GetMapping("/submissions/{submissionId}")
    public QuickstartSubmissionDTO getSubmission(@PathVariable String submissionId) {
        QuickstartSubmissionDTO submission = quickstartRepository.getSubmission(submissionId);

        if (submission == null) {
            throw new NotFoundException();
        }

        return submission;
    }

}
