package com.n26.transactionstatistics.util

import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.Instant

// Should be loaded from config file
val big_decimal_rounding_mode = HALF_UP
val big_decimal_dcale : Int = 2
val instant_seconds_to_add : Long = 60
val instant_seconds_to_minus : Long = 60

// BigDecimal extension
fun BigDecimal.n26Scale() = this.setScale(big_decimal_dcale, big_decimal_rounding_mode)
fun BigDecimal.n26Devide(other: BigDecimal) = this.divide(other, big_decimal_dcale, big_decimal_rounding_mode)

// Instant extension
fun Instant.n26PlusSeconds() = this.plusSeconds(instant_seconds_to_add)
fun Instant.n26MinusSeconds() = this.minusSeconds(instant_seconds_to_minus)

// Long extension
fun Long.isZero() = this == 0L
