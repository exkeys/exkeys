package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.auth.SignupRequestDto
import com.donait.paymentsimulator.dto.auth.LoginRequestDto
import com.donait.paymentsimulator.dto.auth.TokenResponseDto
import com.donait.paymentsimulator.entity.User
import com.donait.paymentsimulator.repository.UserRepository
import com.donait.paymentsimulator.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : UserDetailsService {

    @Transactional
    fun signup(signupRequest: SignupRequestDto) {
        if (userRepository.existsByUsername(signupRequest.username)) {
            throw IllegalArgumentException("이미 존재하는 사용자명입니다.")
        }

        val user = User(
            username = signupRequest.username,
            password = passwordEncoder.encode(signupRequest.password),
            email = signupRequest.email
        )

        userRepository.save(user)
    }

    @Transactional
    fun login(loginRequest: LoginRequestDto): TokenResponseDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        val user = userRepository.findByUsername(loginRequest.username)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }

        val token = jwtTokenProvider.generateToken(user)
        return TokenResponseDto(token = token, expiresAt = jwtTokenProvider.getExpirationTime())
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            emptyList()
        )
    }
} 