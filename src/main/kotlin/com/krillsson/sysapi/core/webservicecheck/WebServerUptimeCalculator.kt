package com.krillsson.sysapi.core.webservicecheck

import com.krillsson.sysapi.util.round
import com.krillsson.sysapi.util.toOffsetDateTime
import org.springframework.stereotype.Component
import java.time.*
import java.util.concurrent.TimeUnit

@Component
class WebServerUptimeCalculator {
    fun metricsForHistory(historyThisMonth: List<WebServerCheckHistoryEntry>): UptimeMetrics {
        val historyDays = historyThisMonth.groupBy { it.timeStamp.toOffsetDateTime().toLocalDate() }
        val days = historyDays.map { createUptimeDay(it.key, it.value) }
        return UptimeMetrics(
            perDay = days,
            total = days.asTotal()
        )
    }

    private fun createUptimeDay(date: LocalDate, value: List<WebServerCheckHistoryEntry>): UptimeDay {
        val downtimeSeconds = calculateDownTimeSecondsForDate(value)
        val seconds = date.secondsElapsedAtDay()
        val downtimePercent = calculateDownTimePercent(seconds, downtimeSeconds)
        return UptimeDay(
            date.asOffsetDateTimeAtStartOfDay().toInstant(),
            downtimePercent,
            downtimeSeconds,
            seconds
        )
    }

    private fun calculateDownTimeSecondsForDate(eventsThisDay: List<WebServerCheckHistoryEntry>): Long {
        var downtimeSeconds: Long = 0
        eventsThisDay.windowed(2).forEach {
            val first = it.first()
            if (!first.isSuccessful) {
                downtimeSeconds = Duration.between(first.timeStamp, it[1].timeStamp).seconds
            }
        }
        return downtimeSeconds
    }

    private fun calculateDownTimePercent(secondsElapsedAtDay: Long, downTimeSeconds: Long): Double {
        return (downTimeSeconds.toDouble() / secondsElapsedAtDay.toDouble()).round(4)
    }

    private fun List<UptimeDay>.asTotal(): UptimePeriod {
        return when {
            isEmpty() -> {
                val now = Instant.now()
                UptimePeriod(
                    now,
                    now,
                    0,
                    100.0,
                    0
                )
            }

            size == 1 -> {
                UptimePeriod(
                    first().timestampAtStartOfDay,
                    first().timestampAtStartOfDay,
                    0,
                    0.0,
                    0
                )
            }

            else -> {
                val totalDownTimeSeconds = sumOf { it.downTimeSeconds }
                val totalSeconds = sumOf { it.totalSeconds }
                val totalPercent = calculateDownTimePercent(totalSeconds, totalDownTimeSeconds)
                UptimePeriod(
                    first().timestampAtStartOfDay,
                    last().timestampAtStartOfDay,
                    totalDownTimeSeconds,
                    totalPercent, totalSeconds
                )
            }
        }

    }

    private fun LocalDate.secondsElapsedAtDay(): Long {
        return if (isToday()) {
            secondsElapsedToday()
        } else {
            TimeUnit.DAYS.toSeconds(1)
        }
    }

    private fun LocalDate.isToday() = isEqual(LocalDate.now())
    private fun LocalDate.secondsElapsedToday() = Duration.between(
        asOffsetDateTimeAtStartOfDay(),
        OffsetDateTime.now()
    ).seconds

    private fun LocalDate.asOffsetDateTimeAtStartOfDay() = atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime()

}