package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.payment.PaymentConfirmRequestDto
import com.donait.paymentsimulator.dto.payment.PaymentInitRequestDto
import com.donait.paymentsimulator.dto.transaction.TransactionRequestDto
import com.donait.paymentsimulator.dto.transaction.TransactionResponseDto
import com.donait.paymentsimulator.entity.Transaction
import com.donait.paymentsimulator.entity.TransactionStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest {

    @Mock
    private lateinit var transactionService: TransactionService

    @Mock
    private lateinit var accountService: AccountService

    @InjectMocks
    private lateinit var paymentService: PaymentService

    private lateinit var paymentInitRequest: PaymentInitRequestDto
    private lateinit var transactionResponse: TransactionResponseDto
    private lateinit var transaction: Transaction

    @BeforeEach
    fun setUp() {
        paymentInitRequest = PaymentInitRequestDto(
            sourceAccountNumber = "1234567890",
            targetAccountNumber = "0987654321",
            amount = BigDecimal("1000.00"),
            description = "테스트 결제"
        )

        transactionResponse = TransactionResponseDto(
            transactionId = "test-transaction-id",
            sourceAccountNumber = "1234567890",
            targetAccountNumber = "0987654321",
            amount = BigDecimal("1000.00"),
            status = TransactionStatus.PENDING.name,
            description = "테스트 결제",
            createdAt = LocalDateTime.now().toString()
        )
    }

    @Test
    fun `결제 초기화가 성공적으로 이루어져야 한다`() {
        // given
        `when`(transactionService.createTransaction(any())).thenReturn(transactionResponse)

        // when
        val result = paymentService.initPayment(paymentInitRequest)

        // then
        assertThat(result.transactionId).isEqualTo(transactionResponse.transactionId)
        assertThat(result.status).isEqualTo(TransactionStatus.PENDING.name)
        verify(transactionService).createTransaction(any())
    }

    @Test
    fun `결제 확인이 성공적으로 이루어져야 한다`() {
        // given
        val confirmRequest = PaymentConfirmRequestDto(transactionId = "test-transaction-id")
        `when`(transactionService.getTransaction(confirmRequest.transactionId)).thenReturn(transaction)
        `when`(transaction.status).thenReturn(TransactionStatus.PENDING)

        // when
        val result = paymentService.confirmPayment(confirmRequest)

        // then
        verify(transactionService).processTransaction(confirmRequest.transactionId)
        assertThat(result.transactionId).isEqualTo(confirmRequest.transactionId)
    }

    @Test
    fun `이미 처리된 거래에 대한 결제 확인은 실패해야 한다`() {
        // given
        val confirmRequest = PaymentConfirmRequestDto(transactionId = "test-transaction-id")
        `when`(transactionService.getTransaction(confirmRequest.transactionId)).thenReturn(transaction)
        `when`(transaction.status).thenReturn(TransactionStatus.COMPLETED)

        // when & then
        assertThatThrownBy { paymentService.confirmPayment(confirmRequest) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("이미 처리된 거래입니다")
    }

    @Test
    fun `결제 상태 조회가 성공적으로 이루어져야 한다`() {
        // given
        val transactionId = "test-transaction-id"
        `when`(transactionService.getTransaction(transactionId)).thenReturn(transaction)

        // when
        val result = paymentService.getPaymentStatus(transactionId)

        // then
        assertThat(result.transactionId).isEqualTo(transactionId)
        verify(transactionService).getTransaction(transactionId)
    }
} 