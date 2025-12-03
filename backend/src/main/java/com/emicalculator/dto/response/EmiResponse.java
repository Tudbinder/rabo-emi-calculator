package com.emicalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class EmiResponse {
    private BigDecimal emi;
    private BigDecimal totalPayment;
    private BigDecimal totalInterest;
    private BigDecimal monthlyRate;
    private int tenureMonths;
}
