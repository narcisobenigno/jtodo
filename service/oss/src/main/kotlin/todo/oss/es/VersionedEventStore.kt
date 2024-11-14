package todo.oss.es

interface VersionedEventStore {
    fun load(id: Id): List<VersionedEventRecord>

    fun write(newEvents: List<VersionedEventRecord>)
}
