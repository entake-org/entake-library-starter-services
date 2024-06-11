package io.entake.library.heap.presentation.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeapOtherIncomeSourceDTO {
    private String source;
    private String recipient;
    private BigDecimal monthlyAmount;
}
