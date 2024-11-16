package todo.oss.port.es

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import todo.oss.es.ConflictException
import todo.oss.es.EventRecord
import todo.oss.es.EventStream
import todo.oss.es.Id
import todo.oss.es.MultiplyHappened
import todo.oss.es.PersistedEventRecord
import todo.oss.es.Position
import todo.oss.es.StreamQuery
import todo.oss.es.SumHappened
import todo.oss.es.VersionConflictException

class InMemoryEventStoreTest {
    @Test
    fun `appends an event`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized
        )

        assertEquals(
            EventStream(listOf(PersistedEventRecord(
                SumHappened(value = 10),
                listOf(Id.fixed("sum-1")),
                Position.Current(1u)
            ))),
            store.read(StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened"))),
        )
    }

    @Test
    fun `appends an event when already exists one`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.Current(1u),
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(2u),
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
    fun `reads events filtering by stream ids`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized,
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-2")))),
            StreamQuery(listOf(Id.fixed("sum-2")), listOf("SumHappened")),
            Position.Current(1u),
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(1u),
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
    fun `appends event filtering by query event types`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized,
        )

        store.append(
            listOf(EventRecord(MultiplyHappened(value = 15), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("MultiplyHappened")),
            Position.NotInitialized,
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(1u),
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
        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        MultiplyHappened(value = 15),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    listOf(Id.fixed("sum-1")),
                    listOf("MultiplyHappened")
                )
            ),
        )
    }

    @Test
    fun `appends event filtering by query stream id`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized,
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-2")))),
            StreamQuery(listOf(Id.fixed("sum-2")), listOf("SumHappened")),
            Position.NotInitialized,
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(1u),
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
        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        listOf(Id.fixed("sum-2")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    listOf(Id.fixed("sum-2")),
                    listOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `conflicts when position moved`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(EventRecord(SumHappened(value = 10), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.NotInitialized,
        )

        store.append(
            listOf(EventRecord(SumHappened(value = 15), listOf(Id.fixed("sum-1")))),
            StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
            Position.Current(1u),
        )

        assertEquals(
            Result.failure<String>(ConflictException(Position.Current(1u)),),
            store.append(
                listOf(EventRecord(SumHappened(value = 20), listOf(Id.fixed("sum-1")))),
                StreamQuery(listOf(Id.fixed("sum-1")), listOf("SumHappened")),
                Position.Current(1u),
            ),
        )


        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        listOf(Id.fixed("sum-1")),
                        Position.Current(2u),
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
    fun `append multiple events of different types`() {
        val store = InMemoryEventStore()

        store.append(
            listOf(
                EventRecord(SumHappened(value = 10), listOf(Id.fixed("calc-1"))),
                EventRecord(MultiplyHappened(value = 20), listOf(Id.fixed("calc-1")))
            ),
            StreamQuery(listOf(Id.fixed("calc-1")), listOf("SumHappened", "MultiplyHappened")),
            Position.NotInitialized
        )

        assertEquals(
            EventStream(listOf(
                PersistedEventRecord(SumHappened(value = 10), listOf(Id.fixed("calc-1")), Position.Current(1u)),
                PersistedEventRecord(MultiplyHappened(value = 20), listOf(Id.fixed("calc-1")), Position.Current(2u))
            )),
            store.read(StreamQuery(listOf(Id.fixed("calc-1")), listOf("SumHappened", "MultiplyHappened"))),
        )
    }

    @Test
    fun `appends multi streams event`() {
        val store = InMemoryEventStore()

        assertEquals(Result.success("successfully stored 1 events"), store.append(
            listOf(
                EventRecord(SumHappened(value = 10), listOf(Id.fixed("calc-1"), Id.fixed("calc-2"))),
            ),
            StreamQuery(listOf(Id.fixed("calc-1"), Id.fixed("calc-2")), listOf("SumHappened")),
            Position.NotInitialized
        ))
        assertEquals(Result.success("successfully stored 1 events"), store.append(
            listOf(
                EventRecord(SumHappened(value = 20), listOf(Id.fixed("calc-1"), Id.fixed("calc-3"))),
            ),
            StreamQuery(listOf(Id.fixed("calc-1"), Id.fixed("calc-3")), listOf("SumHappened")),
            Position.NotInitialized
        ))

        assertEquals(
            EventStream(listOf(
                PersistedEventRecord(SumHappened(value = 10), listOf(Id.fixed("calc-1"), Id.fixed("calc-2")), Position.Current(1u)),
            )),
            store.read(StreamQuery(listOf(Id.fixed("calc-1"), Id.fixed("calc-2")), listOf("SumHappened"))),
        )
    }
}