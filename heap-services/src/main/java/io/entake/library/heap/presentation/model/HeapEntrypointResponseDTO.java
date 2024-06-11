package io.entake.library.heap.presentation.model;

import lombok.*;

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
    private Long dateOfBirth;
    private HeapAddressDTO homeAddress;
}
