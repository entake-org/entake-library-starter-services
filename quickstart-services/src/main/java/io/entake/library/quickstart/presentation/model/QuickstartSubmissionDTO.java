package io.entake.library.quickstart.presentation.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuickstartSubmissionDTO {

    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String ssn;
    private String gender;
    private String maritalStatus;
    private LocalDateTime dob;
    private String email;
    private String phoneNumber;
    private String phoneType;
    private String degrees;
    private List<AddressDTO> addresses;
    private List<BusinessDTO> businesses;
    private ContactDTO altContact;

    private EntakeMetadataDTO entakeMetadata;

}
