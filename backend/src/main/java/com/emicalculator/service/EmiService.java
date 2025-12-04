package com.emicalculator.service;

import com.emicalculator.dto.request.EmiRequest;
import com.emicalculator.dto.response.EmiResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class EmiService {

    private static final int MONTHS_IN_YEAR = 12;
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final MathContext CALC_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    private static final int CURRENCY_SCALE = 2;

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
        EmiResponse response = new EmiResponse();
        int termInMonths = (int) (req.getLoanTermYears() * MONTHS_IN_YEAR);
        response.setTenureMonths(termInMonths);

        // Convert loan value (P) to BigDecimal
        BigDecimal principal = BigDecimal.valueOf(req.getLoanValue());

        // Convert yearly rate (%) to monthly decimal rate (R)
        // monthlyRateDecimal = (yearlyRate / 100) / 12
        BigDecimal yearlyRateDecimal = BigDecimal.valueOf(req.getYearlyInterestRate()).divide(HUNDRED, CALC_CONTEXT);
        BigDecimal monthlyRateDecimal = yearlyRateDecimal.divide(new BigDecimal(MONTHS_IN_YEAR), CALC_CONTEXT);
        response.setMonthlyRate(monthlyRateDecimal.setScale(8, RoundingMode.HALF_UP));

        // Handle edge case for zero/negative interest rate
        if (monthlyRateDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal totalMonthsBD = new BigDecimal(termInMonths);
            BigDecimal emiZeroRate = principal.divide(totalMonthsBD, CURRENCY_SCALE, RoundingMode.HALF_UP);

            response.setEmi(emiZeroRate);
            response.setTotalPayment(principal.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP));
            response.setTotalInterest(BigDecimal.ZERO.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP));
            return response;
        }

        // (1 + R)
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRateDecimal);

        // (1 + R)^N - Use a standard utility for power calculation
        BigDecimal onePlusRPowerN = this.power(onePlusR, termInMonths);

        // Numerator: P * R * (1+R)^N
        BigDecimal numerator = principal
                .multiply(monthlyRateDecimal, CALC_CONTEXT)
                .multiply(onePlusRPowerN, CALC_CONTEXT);

        // Denominator: (1+R)^N - 1
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);

        // Calculate final EMI, rounded to 2 decimal places (currency)
        BigDecimal emiValue = numerator.divide(denominator, CURRENCY_SCALE, RoundingMode.HALF_UP);
        response.setEmi(emiValue);

        // Total Payment = EMI * N
        BigDecimal totalMonthsBD = new BigDecimal(termInMonths);
        BigDecimal totalPayment = emiValue.multiply(totalMonthsBD);
        response.setTotalPayment(totalPayment.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP));

        // Total Interest = Total Payment - Principal
        BigDecimal totalInterest = totalPayment.subtract(principal);
        response.setTotalInterest(totalInterest.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP));

        return response;
    }

    private BigDecimal power(BigDecimal base, int exponent) {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 0; i < exponent; i++) {
            result = result.multiply(base, CALC_CONTEXT);
        }
        return result;
    }
}

