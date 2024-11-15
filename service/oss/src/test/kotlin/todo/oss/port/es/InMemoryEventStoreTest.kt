package todo.oss.port.es

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import todo.oss.es.EventRecord
import todo.oss.es.EventStream
import todo.oss.es.Id
import todo.oss.es.PersistedEventRecord
import todo.oss.es.StreamQuery
import todo.oss.es.SumHappened

class InMemoryEventStoreTest {
    @Test
    fun `records an event`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            1u
        )

        assertEquals(
            EventStream(listOf(PersistedEventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")), 1u))),
            store.read(StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened"))),
        )
    }

    @Test
    fun `records an event when already exists one`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            1u
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            1u
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        1u
                    ),
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        listOf(Id.fixed("sum-1")),
                        2u
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    listOf(Id.fixed("sum-1")),
                    listOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `records an event when already exists one in other stream`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            1u
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-2")))),
            StreamQuery(listOf(Id.fixed("sum-2")), listOf("SumHappened")),
            1u
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        1u
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    listOf(Id.fixed("sum-1")),
                    listOf("SumHappened")
                )
            ),
        )
    }
}