package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.auth.AuthRequestDto
import com.donait.paymentsimulator.dto.auth.AuthResponseDto
import com.donait.paymentsimulator.entity.User
import com.donait.paymentsimulator.repository.UserRepository
import com.donait.paymentsimulator.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun register(request: AuthRequestDto): AuthResponseDto {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 등록된 이메일입니다.")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )
        userRepository.save(user)

        val token = createToken(user)
        return AuthResponseDto(token)
    }

    @Transactional
    fun login(request: AuthRequestDto): AuthResponseDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val token = createToken(user)
        return AuthResponseDto(token)
    }

    private fun createToken(user: User): String {
        return jwtTokenProvider.createToken(user.email)
    }
} 