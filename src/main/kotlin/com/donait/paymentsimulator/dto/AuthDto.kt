package com.donait.paymentsimulator.dto

import jakarta.validation.constraints.NotBlank

data class SignupRequestDto(
    @field:NotBlank(message = "사용자명은 필수입니다")
    val username: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)

data class LoginRequestDto(
    @field:NotBlank(message = "사용자명은 필수입니다")
    val username: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)

data class AuthResponseDto(
    val token: String,
    val username: String
) 