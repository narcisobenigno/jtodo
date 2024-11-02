package jtodo.oss.es

fun <C : Command, S : State, D : Decider<C, S>> useCase(decider: D, eventStream: EventStream): (Id, C) -> Result<String> {
    return { id: Id, command: C ->
        val events = eventStream.load(id)
        val state = events.fold(decider.initialState, decider::evolve)

        decider.decide(command, state)
            .onSuccess(eventStream::write)
            .map { "saved ${it.size} events"  }
    }
}