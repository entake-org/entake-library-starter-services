package io.entake.library.quickstart.presentation.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String attention;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String state;
    private String zipcode;
    private String zipPlus4;
    private String latitude;
    private String longitude;
    private Integer yearsOwned;
}
