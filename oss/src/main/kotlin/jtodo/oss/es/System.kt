package jtodo.oss.es

import java.util.UUID

fun <C : Command, S : State, D : Decider<C, S>> system(decider: D, eventStream: EventStream): (UUID, C) -> Unit {
    return { id: UUID, command: C ->
        val events = eventStream.load(id)
        val state = events.fold(decider.initialState, decider::evolve)
        val result = decider.decide(command, state)
        eventStream.write(result)
    }
}