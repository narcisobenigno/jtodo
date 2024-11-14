package todo.oss.es

interface Command

interface State

interface Event {
    val eventName: String
}

interface Decider<C : Command, S : State> {
    fun decide(
        command: C,
        state: S,
    ): Result<List<VersionedEventRecord>>

    fun evolve(
        state: S,
        record: VersionedEventRecord,
    ): S

    val initialState: S
}
