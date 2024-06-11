package io.entake.library.quickstart.presentation.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private List<String> errors;
}
