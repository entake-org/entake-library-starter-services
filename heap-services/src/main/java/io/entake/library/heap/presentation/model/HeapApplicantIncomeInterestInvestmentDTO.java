package io.entake.library.heap.presentation.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapApplicantIncomeInterestInvestmentDTO {
    private String accountType;
    private BigDecimal amountReceivedYtd;
    private String sourceName;
}
