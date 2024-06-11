package io.entake.library.heap.presentation.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapAddressDTO {
    private String attention;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String state;
    private String zipcode;
    private String zipPlus4;
    private String latitude;
    private String longitude;
    private String livedAmount;
}
