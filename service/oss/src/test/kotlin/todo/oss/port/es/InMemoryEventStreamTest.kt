package todo.oss.port.es

import jtodo.oss.es.*
import jtodo.oss.port.es.InMemoryEventStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import todo.oss.es.SumHappened

class InMemoryEventStreamTest  {
    @Nested
    @DisplayName("complies with event stream")
    inner class ContractTest: EventStreamContractTest {
        override fun eventStream(): EventStream {
            return InMemoryEventStream()
        }
    }

    @Test
    fun `writes events in constructor`() {
        val inMemoryEventStream = InMemoryEventStream(
            EventRecord(
                Id.fixed("recipe-1"),
                Version(),
                SumHappened(value = 1),
            )
        )

        val events = inMemoryEventStream.load(Id.fixed("recipe-1"))
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
