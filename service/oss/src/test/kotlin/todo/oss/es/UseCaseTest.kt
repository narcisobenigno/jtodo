package todo.oss.es

import todo.oss.port.es.InMemoryVersionedEventStore
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
                VersionedEventRecord(
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
                VersionedEventRecord(
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
                VersionedEventRecord(
                    Id.fixed("something-1"),
                    Version(1u),
                    SumHappened(value = 2),
                ),
                VersionedEventRecord(
                    Id.fixed("something-1"),
                    Version(2u),
                    SumHappened(value = 5),
                ),
            ),
            eventStore.load(Id.fixed("something-1")),
        )
    }
}
