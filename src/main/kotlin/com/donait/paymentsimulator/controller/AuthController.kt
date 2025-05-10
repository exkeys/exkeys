package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.dto.auth.AuthRequestDto
import com.donait.paymentsimulator.dto.auth.AuthResponseDto
import com.donait.paymentsimulator.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        return ResponseEntity.ok(authService.login(request))
    }
} 