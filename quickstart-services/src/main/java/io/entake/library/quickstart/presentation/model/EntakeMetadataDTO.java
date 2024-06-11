package io.entake.library.quickstart.presentation.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntakeMetadataDTO {
    private String submissionId;
    private String submissionLocale;
    private String submissionUser;
    private OffsetDateTime submissionDate;
    private String submissionStatus;
    private OffsetDateTime decisionDate;
    private String decisionOwner;
}
