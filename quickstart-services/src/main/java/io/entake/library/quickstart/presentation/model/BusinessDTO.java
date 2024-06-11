package io.entake.library.quickstart.presentation.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO {
    private String name;
    private String fein;
    private Integer percentOwnership;
    private String stateOfIncorporation;
    private List<AddressDTO> addresses;
}
