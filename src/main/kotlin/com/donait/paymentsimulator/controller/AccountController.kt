package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.dto.account.AccountCreateRequestDto
import com.donait.paymentsimulator.dto.account.AccountResponseDto
import com.donait.paymentsimulator.service.AccountService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService
) {
    @PostMapping
    fun createAccount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: AccountCreateRequestDto
    ): ResponseEntity<AccountResponseDto> {
        val userId = userDetails.username.toLong()
        val account = accountService.createAccount(userId, request)
        return ResponseEntity.ok(account)
    }

    @GetMapping
    fun getAccounts(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<AccountResponseDto>> {
        val userId = userDetails.username.toLong()
        val accounts = accountService.getAccountsByUserId(userId)
        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathVariable accountNumber: String): ResponseEntity<AccountResponseDto> {
        val account = accountService.getAccountByAccountNumber(accountNumber)
        return ResponseEntity.ok(account)
    }
} 