package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.dto.payment.PaymentInitiateRequestDto
import com.donait.paymentsimulator.dto.payment.PaymentConfirmRequestDto
import com.donait.paymentsimulator.dto.payment.PaymentStatusResponseDto
import com.donait.paymentsimulator.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping("/initiate")
    fun initiatePayment(@RequestBody request: PaymentInitiateRequestDto): ResponseEntity<PaymentStatusResponseDto> {
        return ResponseEntity.ok(paymentService.initiatePayment(request))
    }

    @PostMapping("/confirm")
    fun confirmPayment(@RequestBody request: PaymentConfirmRequestDto): ResponseEntity<PaymentStatusResponseDto> {
        return ResponseEntity.ok(paymentService.confirmPayment(request.transactionId))
    }

    @GetMapping("/{transactionId}")
    fun getPaymentStatus(@PathVariable transactionId: String): ResponseEntity<PaymentStatusResponseDto> {
        return ResponseEntity.ok(paymentService.getPaymentStatus(transactionId))
    }
} 