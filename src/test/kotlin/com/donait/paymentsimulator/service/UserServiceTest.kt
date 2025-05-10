package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.model.User
import com.donait.paymentsimulator.repository.UserRepository
import com.donait.paymentsimulator.security.JwtTokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `should create user successfully`() {
        // given
        val username = "testuser"
        val password = "password"
        val encodedPassword = "encodedPassword"
        val user = User(username = username, passwordHash = encodedPassword)

        `when`(userRepository.existsByUsername(username)).thenReturn(false)
        `when`(passwordEncoder.encode(password)).thenReturn(encodedPassword)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        // when
        val result = userService.createUser(username, password)

        // then
        assertThat(result.username).isEqualTo(username)
        assertThat(result.passwordHash).isEqualTo(encodedPassword)
        verify(userRepository).save(any(User::class.java))
    }

    @Test
    fun `should throw exception when username already exists`() {
        // given
        val username = "testuser"
        val password = "password"

        `when`(userRepository.existsByUsername(username)).thenReturn(true)

        // when & then
        assertThatThrownBy { userService.createUser(username, password) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("이미 존재하는 사용자명입니다.")
    }

    @Test
    fun `should login successfully`() {
        // given
        val username = "testuser"
        val password = "password"
        val token = "jwt.token.here"
        val authentication = mock(Authentication::class.java)

        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenReturn(authentication)
        `when`(jwtTokenProvider.generateToken(authentication)).thenReturn(token)

        // when
        val result = userService.login(username, password)

        // then
        assertThat(result).isEqualTo(token)
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtTokenProvider).generateToken(authentication)
    }
} 