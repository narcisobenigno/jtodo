package todo.oss.es

import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.oss.es.useCase
import jtodo.oss.port.es.InMemoryVersionedEventStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UseCaseTest {
    @Test
    fun `executes command`() {
        val eventStore = InMemoryVersionedEventStore()
        val doSomething = useCase(DoSomeSum(), eventStore)

        doSomething(
            Id.fixed("something-1"),
            DoSum(
                Id.fixed("something-1"),
                3,
            ),
        )

        assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("something-1"),
                    Version(),
                    SumHappened(value = 3),
                ),
            ),
            eventStore.load(Id.fixed("something-1")),
        )
    }

    @Test
    fun `consider previous events`() {
        val eventStore =
            InMemoryVersionedEventStore(
                EventRecord(
                    Id.fixed("something-1"),
                    Version(),
                    SumHappened(value = 2),
                ),
            )
        val doSomething = useCase(DoSomeSum(), eventStore)

        doSomething(
            Id.fixed("something-1"),
            DoSum(
                Id.fixed("something-1"),
                3,
            ),
        )

        assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("something-1"),
                    Version(1u),
                    SumHappened(value = 2),
                ),
                EventRecord(
                    Id.fixed("something-1"),
                    Version(2u),
                    SumHappened(value = 5),
                ),
            ),
            eventStore.load(Id.fixed("something-1")),
        )
    }
}
