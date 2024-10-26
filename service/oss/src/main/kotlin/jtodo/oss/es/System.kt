package jtodo.oss.es

import java.util.UUID

fun <C : Command, S : State, D : Decider<C, S>> system(decider: D, eventStream: EventStream): (UUID, C) -> Result<String> {
    return { id: UUID, command: C ->
        val events = eventStream.load(id)
        val state = events.fold(decider.initialState, decider::evolve)

        decider.decide(command, state)
            .onSuccess(eventStream::write)
            .map { "saved ${it.size} events"  }
    }
}