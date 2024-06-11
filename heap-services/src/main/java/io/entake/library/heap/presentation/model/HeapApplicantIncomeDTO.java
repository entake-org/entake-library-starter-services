package io.entake.library.heap.presentation.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapApplicantIncomeDTO {
    private String source;
    private String employer;
    private String payFrequency;
    private BigDecimal grossAmount;
    private BigDecimal medicarePartBGrossAmount;
    private BigDecimal medicarePartDGrossAmount;
    private String rentalIncomeFlag;
    private BigDecimal rentalIncomeAmount;
    private String roomBoardFlag;
    private BigDecimal roomBoardAmount;
    private String selfEmploymentFlag;
    private String selfEmploymentType;
    private String preferredCalculationType;
    private List<HeapApplicantIncomeInterestInvestmentDTO> interestInvestment;
}
