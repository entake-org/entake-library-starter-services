package io.entake.library.heap.presentation.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapApplicantDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String ssn;
    private String sex;
    private String gender;
    private String aliasesFlag;
    private String citizenOrQualifiedNonCitizenFlag;
    private String latinoFlag;
    private String race;
    private String homeMailingAddressSameFlag;
    private String phone;
    private String bestContactTime;
    private String interviewPreference;
    private String spokenLanguage;
    private String readLanguage;
    private String interpreterRequiredFlag;
    private String appliedToHeapFlag;
    private LocalDateTime heapApplicationDate;
    private String snapOrTaFlag;
    private String snapOrTaCaseNumber;
    private List<HeapApplicantAliasDTO> aliases;
    private HeapAddressDTO streetAddress;
    private HeapAddressDTO mailingAddress;
    private HeapApplicantIncomeDTO income;
}
