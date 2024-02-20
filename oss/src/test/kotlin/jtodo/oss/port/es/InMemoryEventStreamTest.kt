package jtodo.oss.port.es

import jtodo.oss.es.Event
import jtodo.oss.es.EventEnvelop
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.LocalDateTime
import java.util.*

class InMemoryEventStreamTest {

    @Nested
    @DisplayName("write events")
    inner class WriteEvents {
        @Test
        fun single_event() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventEnvelop(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            LocalDateTime.of(2024, 2, 18, 1, 2),
                            SomethingHappened(value = 1),
                    ),
            ))

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventEnvelop(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    LocalDateTime.of(2024, 2, 18, 1, 2),
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )
        }


        @Test
        fun multi_events() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventEnvelop(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            LocalDateTime.of(2024, 2, 18, 1, 2),
                            SomethingHappened(value = 1),
                    ),
                    EventEnvelop(
                            UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            LocalDateTime.of(2024, 2, 18, 1, 2),
                            SomethingHappened(value = 2),
                    ),
            ))

            var events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventEnvelop(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    LocalDateTime.of(2024, 2, 18, 1, 2),
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )

            events = inMemoryEventStream.load(UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventEnvelop(
                                    UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    LocalDateTime.of(2024, 2, 18, 1, 2),
                                    SomethingHappened(value = 2),
                            ),
                    ),
                    events
            )
        }

        @Test
        fun conflicts_when_version_conflicts() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventEnvelop(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            LocalDateTime.of(2024, 2, 18, 1, 2),
                            SomethingHappened(value = 1),
                    ),
            ))

            assertThrowsExactly(VersionConflictException::class.java) {
                inMemoryEventStream.write(listOf(
                        EventEnvelop(
                                UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                1,
                                LocalDateTime.of(2024, 2, 18, 1, 2),
                                SomethingHappened(value = 2),
                        ),
                ))
            }

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventEnvelop(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    LocalDateTime.of(2024, 2, 18, 1, 2),
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )
        }

        @Test
        fun ensures_asc_sorting_by_version() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventEnvelop(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            2,
                            LocalDateTime.of(2024, 2, 18, 1, 3),
                            SomethingHappened(value = 2),
                    ),
                    EventEnvelop(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            LocalDateTime.of(2024, 2, 18, 1, 2),
                            SomethingHappened(value = 1),
                    ),
            ))

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventEnvelop(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    LocalDateTime.of(2024, 2, 18, 1, 2),
                                    SomethingHappened(value = 1),
                            ),
                            EventEnvelop(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    2,
                                    LocalDateTime.of(2024, 2, 18, 1, 3),
                                    SomethingHappened(value = 2),
                            ),
                    ),
                    events,
            )
        }
    }
}

data class SomethingHappened(val value: Int) : Event {
    override val eventName: String
        get() = "SomethingHappened"
}
