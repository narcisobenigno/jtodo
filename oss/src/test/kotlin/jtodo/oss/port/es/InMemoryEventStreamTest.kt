package jtodo.oss.port.es

import jtodo.oss.es.Event
import jtodo.oss.es.EventRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class InMemoryEventStreamTest {

    @Nested
    @DisplayName("write events")
    inner class WriteEvents {
        @Test
        fun `single events`() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventRecord(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            SomethingHappened(value = 1),
                    ),
            ))

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventRecord(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )
        }


        @Test
        fun `multi events`() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventRecord(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            SomethingHappened(value = 1),
                    ),
                    EventRecord(
                            UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            SomethingHappened(value = 2),
                    ),
            ))

            var events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventRecord(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )

            events = inMemoryEventStream.load(UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventRecord(
                                    UUID.fromString("a326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    SomethingHappened(value = 2),
                            ),
                    ),
                    events
            )
        }

        @Test
        fun `conflicts when version conflicts`() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventRecord(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            SomethingHappened(value = 1),
                    ),
            ))

            assertThrowsExactly(VersionConflictException::class.java) {
                inMemoryEventStream.write(listOf(
                        EventRecord(
                                UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                1,
                                SomethingHappened(value = 2),
                        ),
                ))
            }

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventRecord(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    SomethingHappened(value = 1),
                            ),
                    ),
                    events,
            )
        }

        @Test
        fun `ensures asc sorting by version`() {
            val inMemoryEventStream = InMemoryEventStream()

            inMemoryEventStream.write(listOf(
                    EventRecord(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            2,
                            SomethingHappened(value = 2),
                    ),
                    EventRecord(
                            UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                            1,
                            SomethingHappened(value = 1),
                    ),
            ))

            val events = inMemoryEventStream.load(UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"))
            assertEquals(
                    listOf(
                            EventRecord(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    1,
                                    SomethingHappened(value = 1),
                            ),
                            EventRecord(
                                    UUID.fromString("9326449b-3c8e-4ce6-8d0e-d1a2ef96aa12"),
                                    2,
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
