package com.service;

import com.emicalculator.dto.request.EmiRequest;
import com.emicalculator.dto.response.EmiResponse;
import com.emicalculator.service.EmiService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmiServiceTest {

    @Test
    public void testComputeEmi_typicalCase() throws Exception {
        EmiService service = new EmiService();
        EmiRequest req = new EmiRequest();
        req.setLoanValue(100000.0);
        req.setYearlyInterestRate(8.5);
        req.setLoanTermYears(20.0);

        Method method = EmiService.class.getDeclaredMethod("computeEmi", EmiRequest.class);
        method.setAccessible(true);
        EmiResponse resp = (EmiResponse) method.invoke(service, req);

        assertEquals(240, resp.getTenureMonths());
        assertEquals(new BigDecimal("0.00708333"), resp.getMonthlyRate());
        assertEquals(new BigDecimal("867.82"), resp.getEmi());
        assertEquals(new BigDecimal("208276.80"), resp.getTotalPayment());
        assertEquals(new BigDecimal("108276.80"), resp.getTotalInterest());
    }

    @Test
    public void testComputeEmi_zeroInterest() throws Exception {
        EmiService service = new EmiService();
        EmiRequest req = new EmiRequest();
        req.setLoanValue(120000.0);
        req.setYearlyInterestRate(0.0);
        req.setLoanTermYears(10.0);

        Method method = EmiService.class.getDeclaredMethod("computeEmi", EmiRequest.class);
        method.setAccessible(true);
        EmiResponse resp = (EmiResponse) method.invoke(service, req);

        assertEquals(120, resp.getTenureMonths());
        assertEquals(new BigDecimal("0.00000000"), resp.getMonthlyRate());
        assertEquals(new BigDecimal("1000.00"), resp.getEmi());
        assertEquals(new BigDecimal("120000.00"), resp.getTotalPayment());
        assertEquals(new BigDecimal("0.00"), resp.getTotalInterest());
    }

    @Test
    public void testComputeEmi_negativeInterest() throws Exception {
        EmiService service = new EmiService();
        EmiRequest req = new EmiRequest();
        req.setLoanValue(50000.0);
        req.setYearlyInterestRate(-1.0);
        req.setLoanTermYears(5.0);

        Method method = EmiService.class.getDeclaredMethod("computeEmi", EmiRequest.class);
        method.setAccessible(true);
        EmiResponse resp = (EmiResponse) method.invoke(service, req);

        assertEquals(60, resp.getTenureMonths());
        assertEquals(new BigDecimal("-0.00083333"), resp.getMonthlyRate());
        assertEquals(new BigDecimal("833.33"), resp.getEmi());
        assertEquals(new BigDecimal("50000.00"), resp.getTotalPayment());
        assertEquals(new BigDecimal("0.00"), resp.getTotalInterest());
    }

    @Test
    public void testComputeEmi_oneMonthTerm() throws Exception {
        EmiService service = new EmiService();
        EmiRequest req = new EmiRequest();
        req.setLoanValue(10000.0);
        req.setYearlyInterestRate(10.0);
        req.setLoanTermYears(1.0 / 12); // 1 month

        Method method = EmiService.class.getDeclaredMethod("computeEmi", EmiRequest.class);
        method.setAccessible(true);
        EmiResponse resp = (EmiResponse) method.invoke(service, req);

        assertEquals(1, resp.getTenureMonths());
        assertEquals(new BigDecimal("0.00833333"), resp.getMonthlyRate());
        assertEquals(new BigDecimal("10083.33"), resp.getEmi());
        assertEquals(new BigDecimal("10083.33"), resp.getTotalPayment());
        assertEquals(new BigDecimal("83.33"), resp.getTotalInterest());
    }
}