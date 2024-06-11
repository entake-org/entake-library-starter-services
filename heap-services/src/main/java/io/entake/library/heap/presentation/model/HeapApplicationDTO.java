package io.entake.library.heap.presentation.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapApplicationDTO {
    private HeapApplicantDTO applicant;
    private List<HeapHouseholdMemberDTO> householdMembers;
    private HeapHousingInformationDTO housingInformation;
    private List<HeapOtherIncomeSourceDTO> otherIncomeSources;
    private HeapAuthorizedRepresentativeDTO authorizedRepresentative;
    private EntakeMetadataDTO entakeMetadata;
}
