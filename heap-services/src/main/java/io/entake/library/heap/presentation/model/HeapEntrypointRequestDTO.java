package io.entake.library.heap.presentation.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapEntrypointRequestDTO {
    private String firstName;
    private String lastName;
    private OffsetDateTime dateOfBirth;
    private String lastFourSsn;
}
