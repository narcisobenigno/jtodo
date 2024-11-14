package todo.oss.port.es

import jtodo.oss.es.EventRecord
import jtodo.oss.es.VersionedEventStore
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.oss.es.VersionConflictException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import todo.oss.es.SumHappened

interface VersionedVersionedEventStoreContractTest {
    fun eventStore(): VersionedEventStore

    @Test
    fun `single events`() {
        val eventStore = eventStore()

        eventStore.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
        )

        val events = eventStore.load(Id.fixed("recipe-1"))
        assertEquals(
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
        val eventStore = eventStore()

        eventStore.write(
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
            ),
        )

        var events = eventStore.load(Id.fixed("recipe-1"))
        assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
            events,
        )

        events = eventStore.load(Id.fixed("recipe-2"))
        assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("recipe-2"),
                    Version(),
                    SumHappened(value = 2),
                ),
            ),
            events,
        )
    }

    @Test
    fun `conflicts when version conflicts`() {
        val eventStore = eventStore()

        eventStore.write(
            listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
        )

        assertThrowsExactly(VersionConflictException::class.java) {
            eventStore.write(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        Version(),
                        SumHappened(value = 2),
                    ),
                ),
            )
        }

        val events = eventStore.load(Id.fixed("recipe-1"))
        assertEquals(
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
        val eventStore = eventStore()

        eventStore.write(
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
            ),
        )

        val events = eventStore.load(Id.fixed("recipe-1"))
        assertEquals(
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
