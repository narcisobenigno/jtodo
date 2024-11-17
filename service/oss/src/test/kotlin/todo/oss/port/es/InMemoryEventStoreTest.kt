package todo.oss.port.es

import org.junit.jupiter.api.Nested

class InMemoryEventStoreTest {
    @Nested
    inner class ContractTest : EventStoreContractTest {
        override fun store() = InMemoryEventStore()
    }
}