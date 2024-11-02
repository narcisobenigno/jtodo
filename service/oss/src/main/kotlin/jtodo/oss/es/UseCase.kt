package jtodo.oss.es

fun <C : Command, S : State, D : Decider<C, S>> useCase(
    decider: D,
    eventStore: EventStore,
): (Id, C) -> Result<String> {
    return { id: Id, command: C ->
        val events = eventStore.load(id)
        val state = events.fold(decider.initialState, decider::evolve)

        decider.decide(command, state)
            .onSuccess(eventStore::write)
            .map { "saved ${it.size} events" }
    }
}
