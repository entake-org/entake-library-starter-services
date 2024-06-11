package io.entake.library.heap.presentation.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapHouseholdMemberIncomeDTO {
    private String source;
    private String employer;
    private String  payFrequency;
    private BigDecimal grossAmount;
    private String studentFlag;
}
