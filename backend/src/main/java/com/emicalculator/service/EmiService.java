package com.emicalculator.service;

import com.emicalculator.dto.request.EmiRequest;
import com.emicalculator.dto.response.EmiResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class EmiService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);

    public EmiResponse calculateEmi(EmiRequest request){
        //validateRequest(request);

        return computeEmi(request);
    }


    private void validateRequest(EmiRequest req) {
        if (req.getLoanValue() == null || req.getYearlyInterestRate() == null || req.getLoanTermYears() == null) {
            throw new IllegalArgumentException("All inputs are required");
        }
        if (req.getLoanValue() <= 0) {
            throw new IllegalArgumentException("loanValue must be > 0");
        }
        if (req.getYearlyInterestRate() < 0 || req.getYearlyInterestRate() > 100) {
            throw new IllegalArgumentException("yearlyInterestRate must be between 0 and 100");
        }
        if (req.getLoanTermYears() <= 0 || req.getLoanTermYears() > 30) {
            throw new IllegalArgumentException("loanTermYears must be >0 and <=30");
        }
    }

    private EmiResponse computeEmi(EmiRequest req) {
        BigDecimal principal = BigDecimal.valueOf(req.getLoanValue());
        BigDecimal yearlyRate = BigDecimal.valueOf(req.getYearlyInterestRate());
        BigDecimal termYears = BigDecimal.valueOf(req.getLoanTermYears());

        int tenureMonths = termYears.multiply(BigDecimal.valueOf(12)).intValue();
        BigDecimal monthlyRate = yearlyRate
                .divide(BigDecimal.valueOf(100), MC)
                .divide(BigDecimal.valueOf(12), MC);

        BigDecimal emi;

        // If interest rate = 0%, EMI is simple division
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            emi = principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
        } else {
            // Standard EMI formula: P * r * (1+r)^N / ((1+r)^N - 1)
            BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
            BigDecimal pow = onePlusR.pow(tenureMonths, MC);

            BigDecimal numerator = principal.multiply(monthlyRate).multiply(pow);
            BigDecimal denominator = pow.subtract(BigDecimal.ONE);

            emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }

        BigDecimal totalPayment = emi.multiply(BigDecimal.valueOf(tenureMonths)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = totalPayment.subtract(principal).setScale(2, RoundingMode.HALF_UP);

        return new EmiResponse(
                emi,
                totalPayment,
                totalInterest,
                monthlyRate.setScale(6, RoundingMode.HALF_UP),
                tenureMonths
        );
    }
}
