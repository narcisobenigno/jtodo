package todo.oss.es

import jtodo.oss.es.Event
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.port.es.InMemoryEventStream
import jtodo.oss.port.es.VersionConflictException
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

            inMemoryEventStream.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    ),
                )
            )

            val events = inMemoryEventStream.load(Id.fixed("recipe-1"))
            assertEquals(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
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

            inMemoryEventStream.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    ),
                    EventRecord(
                        Id.fixed("recipe-2"),
                        1,
                        SomethingHappened(value = 2),
                    ),
                )
            )

            var events = inMemoryEventStream.load(Id.fixed("recipe-1"))
            assertEquals(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    )
                ),
                events,
            )

            events = inMemoryEventStream.load(Id.fixed("recipe-2"))
            assertEquals(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-2"),
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

            inMemoryEventStream.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    ),
                )
            )

            assertThrowsExactly(VersionConflictException::class.java) {
                inMemoryEventStream.write(
                    listOf(
                        EventRecord(
                            Id.fixed("recipe-1"),
                            1,
                            SomethingHappened(value = 2),
                        ),
                    )
                )
            }

            val events = inMemoryEventStream.load(Id.fixed("recipe-1"))
            assertEquals(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
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

            inMemoryEventStream.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        2,
                        SomethingHappened(value = 2),
                    ),
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    ),
                )
            )

            val events = inMemoryEventStream.load(Id.fixed("recipe-1"))
            assertEquals(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        1,
                        SomethingHappened(value = 1),
                    ),
                    EventRecord(
                        Id.fixed("recipe-1"),
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
