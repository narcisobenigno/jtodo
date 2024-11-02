package todo.oss.port.es

import jtodo.oss.es.EventRecord
import jtodo.oss.es.EventStore
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.oss.port.es.InMemoryEventStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import todo.oss.es.SumHappened

class InMemoryEventStoreTest {
    @Nested
    @DisplayName("complies with event store")
    inner class ContractTest : EventStoreContractTest {
        override fun eventStore(): EventStore {
            return InMemoryEventStore()
        }
    }

    @Test
    fun `writes events in constructor`() {
        val inMemoryEventStore =
            InMemoryEventStore(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )

        val events = inMemoryEventStore.load(Id.fixed("recipe-1"))
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
}
