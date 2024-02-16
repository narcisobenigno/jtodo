package jtodo.oss.es

import java.time.LocalDateTime
import java.util.UUID

interface Command {

}

interface State{

}

interface Event {
    val name: String
}


class EventEnvelop(val id: UUID, val version: Int, val recordedAt: LocalDateTime, val event: Event)

interface Decider<C: Command, S: State> {
    fun decide(command: C, state: S): List<EventEnvelop>
    fun evolve(state:S, event: EventEnvelop): S
    val initialState: S
}

