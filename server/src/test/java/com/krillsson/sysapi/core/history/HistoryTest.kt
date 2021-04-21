package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.util.Clock
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class HistoryTest {
    lateinit var history: HistoryRepository
    lateinit var clock: Clock

    @Mock
    lateinit var load: HistorySystemLoad

    @Mock
    lateinit var store: HistoryStore

    @Before
    @Throws(Exception::class)
    fun setUp() {
        clock = Clock()
        history = HistoryRepository(clock, store)
    }

    @Test
    fun happyPath() {
        val twoMinutesAgo = OffsetDateTime.now().minusMinutes(2)
        clock.useFixedClockAt(twoMinutesAgo)
        history.record(load)
        clock.useFixedClockAt(twoMinutesAgo.plusMinutes(4))
        history.record(load)
        Assert.assertThat(history.get().size, Matchers.`is`(2))
        Assert.assertTrue(history.get()[0].date.isBefore(history.get()[1].date))
    }

    @Test
    fun purgingRemovesStuff() {
        val twoMinutesAgo = OffsetDateTime.now().minusMinutes(2)
        println(OffsetDateTime.now().minusDays(2).toString())
        println(OffsetDateTime.now().plusHours(2).toString())
        clock.useFixedClockAt(twoMinutesAgo)
        history.record(load)
        clock.useFixedClockAt(twoMinutesAgo.plusMinutes(4))
        history.record(load)
        Assert.assertThat(history.get().size, Matchers.`is`(2))
        history.purge(1, ChronoUnit.MINUTES)
        Assert.assertThat(history.get().size, Matchers.`is`(1))
    }
}