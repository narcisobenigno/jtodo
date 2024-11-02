package todo.oss.port.es

import jtodo.oss.es.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import todo.oss.es.SumHappened

interface EventStreamContractTest {
    fun eventStream(): EventStream

    @Test
    fun `single events`() {
        val eventStream = eventStream()

        eventStream.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )
        )

        val events = eventStream.load(Id.fixed("recipe-1"))
        Assertions.assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
            events,
        )
    }


    @Test
    fun `multi events`() {
        val eventStream = eventStream()

        eventStream.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
                EventRecord(
                    Id.fixed("recipe-2"),
                    Version(),
                    SumHappened(value = 2),
                ),
            )
        )

        var events = eventStream.load(Id.fixed("recipe-1"))
        Assertions.assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                )
            ),
            events,
        )

        events = eventStream.load(Id.fixed("recipe-2"))
        Assertions.assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-2"),
                    Version(),
                    SumHappened(value = 2),
                ),
            ),
            events
        )
    }

    @Test
    fun `conflicts when version conflicts`() {
        val eventStream = eventStream()

        eventStream.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )
        )

        Assertions.assertThrowsExactly(VersionConflictException::class.java) {
            eventStream.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        Version(),
                        SumHappened(value = 2),
                    ),
                )
            )
        }

        val events = eventStream.load(Id.fixed("recipe-1"))
        Assertions.assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
            events,
        )
    }

    @Test
    fun `ensures asc sorting by version`() {
        val eventStream = eventStream()

        eventStream.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(2u),
                    SumHappened(value = 2),
                ),
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )
        )

        val events = eventStream.load(Id.fixed("recipe-1"))
        Assertions.assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(2u),
                    SumHappened(value = 2),
                ),
            ),
            events,
        )
    }
}