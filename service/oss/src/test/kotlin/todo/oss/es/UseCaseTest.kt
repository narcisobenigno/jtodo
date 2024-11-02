package todo.oss.es

import jtodo.oss.es.*
import jtodo.oss.port.es.InMemoryEventStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UseCaseTest {
    @Test
    fun `executes command`() {
        val eventStream = InMemoryEventStream()
        val doSomething = useCase(DoSomeSum(), eventStream)

        doSomething(
            Id.fixed("something-1"),
            DoSum(
                Id.fixed("something-1"),
                3
            )
        )

        assertEquals(
            listOf(
                EventRecord(
                    Id.fixed("something-1"),
                    Version(),
                    SumHappened(value = 3),
                )
            ),
            eventStream.load(Id.fixed("something-1"))
        )
    }

    @Test
    fun `consider previous events`() {
        val eventStream = InMemoryEventStream(EventRecord(
            Id.fixed("something-1"),
            Version(),
            SumHappened(value = 2),
        ))
        val doSomething = useCase(DoSomeSum(), eventStream)

        doSomething(
            Id.fixed("something-1"),
            DoSum(
                Id.fixed("something-1"),
                3
            )
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
                )
            ),
            eventStream.load(Id.fixed("something-1"))
        )
    }
}