package todo.oss.es

import todo.oss.es.Command
import todo.oss.es.Decider
import todo.oss.es.Event
import todo.oss.es.EventRecord
import todo.oss.es.Id
import todo.oss.es.State
import todo.oss.es.Version

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
        record: EventRecord,
    ): SumState {
        when (val payload = record.event) {
            is SumHappened -> return SumState(state.currentValue + payload.value)
        }

        return state
    }

    override val initialState: SumState
        get() = SumState()
}
