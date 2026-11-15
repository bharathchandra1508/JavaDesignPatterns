package com.designpatterns.samples.strategypattern.controller;

import com.designpatterns.samples.strategypattern.dto.PaymentRequest;
import com.designpatterns.samples.strategypattern.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> pay(@RequestBody PaymentRequest request) {
        paymentService.process(request);
        return ResponseEntity.ok().build();
    }
}