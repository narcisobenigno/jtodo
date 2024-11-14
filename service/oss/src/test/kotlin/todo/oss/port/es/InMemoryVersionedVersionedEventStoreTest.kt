package todo.oss.port.es

import todo.oss.es.VersionedEventRecord
import todo.oss.es.VersionedEventStore
import todo.oss.es.Id
import todo.oss.es.Version
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
                VersionedEventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            )

        val events = inMemoryVersionedEventStore.load(Id.fixed("recipe-1"))
        assertEquals(
            listOf(
                VersionedEventRecord(
                    Id.fixed("recipe-1"),
                    Version(),
                    SumHappened(value = 1),
                ),
            ),
            events,
        )
    }
}
