package com.donait.paymentsimulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaymentSimulatorApplication

fun main(args: Array<String>) {
    runApplication<PaymentSimulatorApplication>(*args)
} 