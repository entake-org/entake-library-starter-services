package io.entake.library.heap.presentation.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntakeMetadataDTO {
    private String submissionId;
    private String submissionLocale;
    private LocalDateTime submissionDate;
    private String submissionUser;
    private String submissionStatus;
    private LocalDateTime decisionDate;
    private String decisionOwner;
    private String environment;
    private List<EntakeMetadataDocumentDTO> documents;
}
