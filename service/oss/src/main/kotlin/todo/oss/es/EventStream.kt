package todo.oss.es

data class PersistedEventRecord(val event: Event, val streamIds: Set<Id>, val position: Position)

data class EventStream(private val events: List<PersistedEventRecord>) {
    val lastPosition = events.lastOrNull()?.position ?: Position.NotInitialized
}
