package io.entake.library.quickstart.presentation.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
