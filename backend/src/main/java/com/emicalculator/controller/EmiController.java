package com.emicalculator.controller;

import com.emicalculator.dto.request.EmiRequest;
import com.emicalculator.dto.response.EmiResponse;
import com.emicalculator.service.EmiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emi")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class EmiController {
    private final EmiService emiService;

    @PostMapping
    public ResponseEntity<EmiResponse> calculateEmi(@Valid @RequestBody EmiRequest request){
        return ResponseEntity.ok(emiService.calculateEmi(request));
    }
}
