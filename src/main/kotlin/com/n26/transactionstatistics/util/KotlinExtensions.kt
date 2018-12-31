package com.n26.transactionstatistics.util

import com.n26.transactionstatistics.util.ApplicationConstantes.Companion.big_decimal_rounding_mode
import com.n26.transactionstatistics.util.ApplicationConstantes.Companion.big_decimal_scale
import com.n26.transactionstatistics.util.ApplicationConstantes.Companion.delay_to_expire
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.Instant

class ApplicationConstantes {
    companion object {
        val big_decimal_scale: Int = 2
        val delay_to_expire: Long = 60
        val big_decimal_rounding_mode = HALF_UP
    }
}

// BigDecimal extension
fun BigDecimal.n26Scale() = this.setScale(big_decimal_scale, big_decimal_rounding_mode)
fun BigDecimal.n26Devide(other: BigDecimal) = this.divide(other, big_decimal_scale, big_decimal_rounding_mode)

// Instant extension
fun Instant.n26PlusSeconds() = this.plusSeconds(delay_to_expire)

fun Instant.n26MinusSeconds() = this.minusSeconds(delay_to_expire)

// Long extension
fun Long.isZero() = this == 0L
