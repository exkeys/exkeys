package com.donait.paymentsimulator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserTest {
    @Test
    fun `should create user with valid data`() {
        // given
        val username = "testuser"
        val passwordHash = "hashedPassword"
        val createdAt = LocalDateTime.now()

        // when
        val user = User(
            username = username,
            passwordHash = passwordHash,
            createdAt = createdAt
        )

        // then
        assertThat(user.id).isEqualTo(0)
        assertThat(user.username).isEqualTo(username)
        assertThat(user.passwordHash).isEqualTo(passwordHash)
        assertThat(user.createdAt).isEqualTo(createdAt)
    }

    @Test
    fun `should create user with default created at time`() {
        // given
        val username = "testuser"
        val passwordHash = "hashedPassword"

        // when
        val user = User(
            username = username,
            passwordHash = passwordHash
        )

        // then
        assertThat(user.id).isEqualTo(0)
        assertThat(user.username).isEqualTo(username)
        assertThat(user.passwordHash).isEqualTo(passwordHash)
        assertThat(user.createdAt).isNotNull()
    }
} 