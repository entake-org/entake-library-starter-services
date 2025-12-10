package io.entake.library.heap.presentation.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapEntrypointResponseDTO {
    private String originalCaseNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private HeapAddressDTO homeAddress;
}
