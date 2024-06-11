package io.entake.library.heap.presentation.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapHousingInformationDTO {
    private String homeType;
    private BigDecimal monthlyPayment;
    private String otherLivingSituation;
    private Integer multiFamilyCount;
    private String housingComplex;
    private String payingForHeatFlag;
    private String heatSource;
    private String fuelTankType;
    private String heatingBillInApplicantNameFlag;
    private String nameOnHeatingBill;
    private String relationshipForHeatingBill;
    private String applicantResponsibleForHeatingBillFlag;
    private String heatingAccountNumber;
    private String heatIncluded;
    private String heatingCompany;
    private String heatingCompanyAddress;
    private String electricityIncluded;
    private String separateElectricBillFlag;
    private String electricBillInApplicantNameFlag;
    private String nameOnElectricBill;
    private String electricAccountNumber;
    private String electricCompany;
    private String electricCompanyAddress;
    private String electricityNeededForFurnaceFlag;
    private String electricityNeededForThermostatFlag;
}
