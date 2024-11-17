package todo.oss.es

interface EventStore {
    fun append(events: List<EventRecord>, streamQuery: StreamQuery, lastPosition: Position): Result<String>
    fun read(streamQuery: StreamQuery): EventStream
}