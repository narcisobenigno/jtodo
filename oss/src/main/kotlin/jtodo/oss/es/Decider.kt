package jtodo.oss.es

import java.time.LocalDateTime
import java.util.UUID

interface Command {

}

interface State{

}

interface Event {
    val eventName: String
}


data class EventEnvelop(val id: UUID, val version: Int, val event: Event)

interface Decider<C: Command, S: State> {
    fun decide(command: C, state: S): Result<List<EventEnvelop>>
    fun evolve(state:S, event: EventEnvelop): S
    val initialState: S
}

