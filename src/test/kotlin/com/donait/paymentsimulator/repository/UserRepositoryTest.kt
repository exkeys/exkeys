package com.donait.paymentsimulator.repository

import com.donait.paymentsimulator.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find user by username`() {
        // given
        val username = "testuser"
        val passwordHash = "hashedPassword"
        val user = User(username = username, passwordHash = passwordHash)

        // when
        userRepository.save(user)
        val foundUser = userRepository.findByUsername(username)

        // then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().username).isEqualTo(username)
        assertThat(foundUser.get().passwordHash).isEqualTo(passwordHash)
    }

    @Test
    fun `should check if username exists`() {
        // given
        val username = "testuser"
        val user = User(username = username, passwordHash = "hashedPassword")

        // when
        userRepository.save(user)
        val exists = userRepository.existsByUsername(username)

        // then
        assertThat(exists).isTrue()
    }

    @Test
    fun `should return false for non-existent username`() {
        // given
        val username = "nonexistentuser"

        // when
        val exists = userRepository.existsByUsername(username)

        // then
        assertThat(exists).isFalse()
    }
} 