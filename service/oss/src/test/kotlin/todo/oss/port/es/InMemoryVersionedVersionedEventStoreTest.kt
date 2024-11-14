package todo.oss.port.es

import jtodo.oss.es.EventRecord
import jtodo.oss.es.VersionedEventStore
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.oss.port.es.InMemoryVersionedEventStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import todo.oss.es.SumHappened

class InMemoryVersionedVersionedEventStoreTest {
    @Nested
    @DisplayName("complies with event store")
    inner class ContractTest : VersionedVersionedEventStoreContractTest {
        override fun eventStore(): VersionedEventStore {
            return InMemoryVersionedEventStore()
        }
    }

    @Test
    fun `writes events in constructor`() {
        val inMemoryVersionedEventStore =
            InMemoryVersionedEventStore(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )

        val events = inMemoryVersionedEventStore.load(Id.fixed("recipe-1"))
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
