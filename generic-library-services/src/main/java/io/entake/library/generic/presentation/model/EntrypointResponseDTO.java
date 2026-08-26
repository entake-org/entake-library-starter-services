package io.entake.library.generic.presentation.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrypointResponseDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private AddressDTO homeAddress;
    private List<AddressDTO> addresses;
}
