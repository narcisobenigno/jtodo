package todo.oss.es

interface VersionedEventStore {
    fun load(id: Id): List<EventRecord>

    fun write(newEvents: List<EventRecord>)
}
