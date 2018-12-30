package com.n26.transactionstatistics.statistics.domain.port.primary

import com.n26.transactionstatistics.statistics.domain.Element
import com.n26.transactionstatistics.statistics.domain.Statistics
import com.n26.transactionstatistics.util.isZero
import com.n26.transactionstatistics.util.n26Devide
import com.n26.transactionstatistics.util.n26Scale
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.SECONDS

class StatisticsStore(private val executor: ScheduledExecutorService,
                      private var sum : BigDecimal = ZERO,
                      private var min : BigDecimal = ZERO,
                      private var max : BigDecimal = ZERO) {

    private val elements = ArrayList<Element>()

    @Synchronized
    fun add(element: Element): Boolean {
        beforeAdd(element)
        return elements.add(element)
    }

    @Synchronized
    fun evict(element: Element) {
        elements.remove(element)
        afterEvict(element)
    }

    @Synchronized
    fun clear() {
        elements.clear()
        afterClear()
    }

    fun scheduleEvict(element: Element, delay: Long) {
        executor.schedule(element, delay, SECONDS)
    }

    fun has(element: Element): Boolean = elements.contains(element)

    private fun beforeAdd(element: Element) {
        sum = sum.add(element.amount)
        min = if (count().isZero()) element.amount
            else if (element.amount < min) element.amount
            else min
        max = if (count().isZero()) element.amount
            else if (element.amount > max) element.amount
            else max
    }

    private fun afterEvict(element: Element) {
        sum = sum.minus(element.amount)
        min = elements.stream()
                .map { e -> e.amount }
                .min { a, b -> a.compareTo(b) }
                .orElse(ZERO)
        max = elements.stream()
                .map { e -> e.amount }
                .max { a, b -> a.compareTo(b) }
                .orElse(ZERO)
    }

    private fun afterClear() {
        sum = ZERO
        min = ZERO
        max = ZERO
    }

    fun sum(): BigDecimal = sum.n26Scale()

    fun avg(): BigDecimal = if (count().isZero()) ZERO.n26Scale() else sum.n26Devide(valueOf(count()))

    fun min(): BigDecimal = min.n26Scale()

    fun max(): BigDecimal = max.n26Scale()

    fun count(): Long = elements.size.toLong()

    fun statistics(): Statistics = Statistics(sum(), avg(), max(), min(), count())

}
