package io.entake.library.heap.presentation.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapHouseholdMemberDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String gender;
    private String latinoFlag;
    private String race;
    private OffsetDateTime dateOfBirth;
    private String ssn;
    private String citizenOrQualifiedNonCitizenFlag;
    private String applicantRelationship;
    private String snapOrTaFlag;
    private String snapOrTaCaseNumber;
    private String blindOrDisabledFlag;
    private String veteranFlag;
    private HeapHouseholdMemberIncomeDTO income;
}
