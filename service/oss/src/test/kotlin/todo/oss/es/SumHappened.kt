package todo.oss.es

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.Event
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.State
import jtodo.oss.es.Version

data class SumHappened(val value: Int) : Event {
    override val eventName: String
        get() = "SumHappened"
}

data class DoSum(val id: Id, val value: Int) : Command

data class SumState(val currentValue: Int = 0, val version: Version = Version()) : State

class DoSomeSum : Decider<DoSum, SumState> {
    override fun decide(
        command: DoSum,
        state: SumState,
    ): Result<List<EventRecord>> {
        if (state == initialState) {
            return Result.success(
                listOf(
                    EventRecord(
                        command.id,
                        Version(),
                        SumHappened(command.value),
                    ),
                ),
            )
        }

        return Result.success(
            listOf(
                EventRecord(
                    command.id,
                    state.version.bump(),
                    SumHappened(command.value + state.currentValue),
                ),
            ),
        )
    }

    override fun evolve(
        state: SumState,
        event: EventRecord,
    ): SumState {
        when (val payload = event.event) {
            is SumHappened -> return SumState(state.currentValue + payload.value)
        }

        return state
    }

    override val initialState: SumState
        get() = SumState()
}
