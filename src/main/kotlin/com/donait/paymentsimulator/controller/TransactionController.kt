package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.dto.transaction.TransactionDto
import com.donait.paymentsimulator.dto.transaction.TransactionListResponseDto
import com.donait.paymentsimulator.entity.TransactionStatus
import com.donait.paymentsimulator.service.TransactionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {
    @GetMapping("/{id}")
    fun getTransaction(@PathVariable id: String): ResponseEntity<TransactionDto> {
        return ResponseEntity.ok(transactionService.getTransaction(id))
    }

    @PostMapping("/{transactionId}/process")
    fun processTransaction(@PathVariable transactionId: String): ResponseEntity<TransactionDto> {
        return ResponseEntity.ok(transactionService.processTransaction(transactionId))
    }

    @GetMapping("/account/{accountNumber}")
    fun getAccountTransactions(
        @PathVariable accountNumber: String,
        @RequestParam(required = false) status: TransactionStatus?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime?
    ): ResponseEntity<TransactionListResponseDto> {
        val transactions = transactionService.getAccountTransactions(accountNumber, status, startDate, endDate)
        return ResponseEntity.ok(TransactionListResponseDto(transactions, transactions.size))
    }
} 