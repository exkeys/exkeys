package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.payment.PaymentInitiateRequestDto
import com.donait.paymentsimulator.dto.payment.PaymentStatusResponseDto
import com.donait.paymentsimulator.entity.Transaction
import com.donait.paymentsimulator.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Service
class PaymentService(
    private val transactionService: TransactionService,
    private val transactionRepository: TransactionRepository
) {
    @Transactional
    fun initiatePayment(request: PaymentInitiateRequestDto): PaymentStatusResponseDto {
        val transaction = transactionService.createTransaction(
            sourceAccountNumber = request.sourceAccountNumber,
            targetAccountNumber = request.targetAccountNumber,
            amount = request.amount,
            description = request.description
        )
        return PaymentStatusResponseDto.fromEntity(transaction)
    }

    @Transactional
    fun confirmPayment(transactionId: String): PaymentStatusResponseDto {
        val transaction = transactionService.processTransaction(transactionId)
        return PaymentStatusResponseDto.fromEntity(transaction)
    }

    @Transactional(readOnly = true)
    fun getPaymentStatus(transactionId: String): PaymentStatusResponseDto {
        val transaction = transactionRepository.findById(transactionId.toLong())
            .orElseThrow { NoSuchElementException("거래를 찾을 수 없습니다: $transactionId") }
        return PaymentStatusResponseDto.fromEntity(transaction)
    }
} 