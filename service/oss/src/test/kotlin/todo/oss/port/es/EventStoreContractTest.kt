package todo.oss.port.es

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import todo.oss.es.ConflictException
import todo.oss.es.EventRecord
import todo.oss.es.EventStore
import todo.oss.es.EventStream
import todo.oss.es.Id
import todo.oss.es.MultiplyHappened
import todo.oss.es.PersistedEventRecord
import todo.oss.es.Position
import todo.oss.es.StreamQuery
import todo.oss.es.SumHappened

interface EventStoreContractTest {
    fun store(): EventStore

    @Test
    fun `appends an event`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10), setOf(Id.fixed("sum-1")), Position.Current(1u)
                    )
                )
            ),
            store.read(StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened"))),
        )
    }

    @Test
    fun `appends an event when already exists one`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 15), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.Current(1u),
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-1")), setOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `reads events filtering by stream ids`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 15), setOf(Id.fixed("sum-2")))),
                StreamQuery(setOf(Id.fixed("sum-2")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-1")), setOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `reads events filtering by any matching stream ids`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1"), Id.fixed("sum-2")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(
                    EventRecord(
                        MultiplyHappened(value = 15),
                        setOf(Id.fixed("sum-1"), Id.fixed("sum-2"), Id.fixed("sum-3"))
                    )
                ),
                StreamQuery(setOf(Id.fixed("sum-2")), setOf("MultiplyHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1"), Id.fixed("sum-2")),
                        Position.Current(1u),
                    ),
                    PersistedEventRecord(
                        MultiplyHappened(value = 15),
                        setOf(Id.fixed("sum-1"), Id.fixed("sum-2"), Id.fixed("sum-3")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-1"), Id.fixed("sum-2")), setOf("SumHappened", "MultiplyHappened")
                )
            ),
        )
    }

    @Test
    fun `appends event filtering by query event types`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(MultiplyHappened(value = 15), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("MultiplyHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                )
            ),
            store.read(StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened"))),
        )
        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        MultiplyHappened(value = 15),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(StreamQuery(setOf(Id.fixed("sum-1")), setOf("MultiplyHappened"))),
        )
    }

    @Test
    fun `appends event filtering by query stream id`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 15), setOf(Id.fixed("sum-2")))),
                StreamQuery(setOf(Id.fixed("sum-2")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-1")), setOf("SumHappened")
                )
            ),
        )
        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        setOf(Id.fixed("sum-2")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-2")), setOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `conflicts when position moved`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 10), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.NotInitialized,
            )
        )

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(EventRecord(SumHappened(value = 15), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.Current(1u),
            )
        )

        assertEquals(
            Result.failure<String>(ConflictException(Position.Current(1u))), store.append(
                listOf(EventRecord(SumHappened(value = 20), setOf(Id.fixed("sum-1")))),
                StreamQuery(setOf(Id.fixed("sum-1")), setOf("SumHappened")),
                Position.Current(1u),
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(1u),
                    ),
                    PersistedEventRecord(
                        SumHappened(value = 15),
                        setOf(Id.fixed("sum-1")),
                        Position.Current(2u),
                    ),
                )
            ),
            store.read(
                StreamQuery(
                    setOf(Id.fixed("sum-1")), setOf("SumHappened")
                )
            ),
        )
    }

    @Test
    fun `append multiple events of different types`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 2 events"), store.append(
                listOf(
                    EventRecord(SumHappened(value = 10), setOf(Id.fixed("calc-1"))),
                    EventRecord(MultiplyHappened(value = 20), setOf(Id.fixed("calc-1")))
                ),
                StreamQuery(setOf(Id.fixed("calc-1")), setOf("SumHappened", "MultiplyHappened")),
                Position.NotInitialized
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(SumHappened(value = 10), setOf(Id.fixed("calc-1")), Position.Current(1u)),
                    PersistedEventRecord(MultiplyHappened(value = 20), setOf(Id.fixed("calc-1")), Position.Current(2u))
                )
            ),
            store.read(StreamQuery(setOf(Id.fixed("calc-1")), setOf("SumHappened", "MultiplyHappened"))),
        )
    }

    @Test
    fun `appends multi streams event`() {
        val store = store()

        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(
                    EventRecord(SumHappened(value = 10), setOf(Id.fixed("calc-1"), Id.fixed("calc-2"))),
                ),
                StreamQuery(setOf(Id.fixed("calc-1"), Id.fixed("calc-2")), setOf("SumHappened")),
                Position.NotInitialized
            )
        )
        assertEquals(
            Result.success("successfully stored 1 events"), store.append(
                listOf(
                    EventRecord(SumHappened(value = 20), setOf(Id.fixed("calc-3"), Id.fixed("calc-3"))),
                ),
                StreamQuery(setOf(Id.fixed("calc-3"), Id.fixed("calc-4")), setOf("SumHappened")),
                Position.NotInitialized
            )
        )

        assertEquals(
            EventStream(
                listOf(
                    PersistedEventRecord(
                        SumHappened(value = 10), setOf(Id.fixed("calc-1"), Id.fixed("calc-2")), Position.Current(1u)
                    ),
                )
            ),
            store.read(StreamQuery(setOf(Id.fixed("calc-1"), Id.fixed("calc-2")), setOf("SumHappened"))),
        )
    }

}