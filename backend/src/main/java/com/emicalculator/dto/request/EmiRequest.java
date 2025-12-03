package com.emicalculator.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class EmiRequest {
    @NotNull(message = "Loan value is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Loan value must be a positive number")
    private Double loanValue;

    @NotNull(message = "Yearly interest rate is required")
    @Min(value = 0, message = "Yearly interest rate must be between 0 and 100")
    @Max(value = 100, message = "Yearly interest rate must be between 0 and 100")
    private Double yearlyInterestRate;

    @NotNull(message = "Loan term (years) is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Loan term must be a positive number")
    @Max(value = 30, message = "Loan term must not exceed 30 years")
    private Double loanTermYears;
}
