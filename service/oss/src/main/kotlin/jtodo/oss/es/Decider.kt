package jtodo.oss.es

import java.util.UUID

interface Command {

}

interface State{

}

interface Event {
    val eventName: String
}


data class EventRecord(val id: UUID, val version: Int, val event: Event)

interface Decider<C: Command, S: State> {
    fun decide(command: C, state: S): Result<List<EventRecord>>
    fun evolve(state:S, event: EventRecord): S
    val initialState: S
}

