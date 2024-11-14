package todo.oss.es

fun <C : Command, S : State, D : Decider<C, S>> useCase(
    decider: D,
    versionedEventStore: VersionedEventStore,
): (Id, C) -> Result<String> {
    return { id: Id, command: C ->
        val events = versionedEventStore.load(id)
        val state = events.fold(decider.initialState, decider::evolve)

        decider.decide(command, state)
            .onSuccess(versionedEventStore::write)
            .map { "saved ${it.size} events" }
    }
}
