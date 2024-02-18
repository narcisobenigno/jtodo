package jtodo.oss.port.es

import jtodo.oss.es.Event
import jtodo.oss.es.EventEnvelop
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class InMemoryEventStreamTest {
    @Test
    fun writes_events() {
        val inMemoryEventStream = InMemoryEventStream()

        inMemoryEventStream.write(listOf(
                EventEnvelop(
                        UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                        1,
                        LocalDateTime.of(2024, 2, 18, 1, 2),
                        SomethingHappened(value = 1),
                )
        ))

        val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
        assertEquals(
                listOf(
                        EventEnvelop(
                                UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                1,
                                LocalDateTime.of(2024, 2, 18, 1, 2),
                                SomethingHappened(value = 1),
                        )
                ),
                events,
        )
    }
}

data class SomethingHappened(val value: Int) : Event {
    override val eventName: String
        get() = "SomethingHappened"
}
