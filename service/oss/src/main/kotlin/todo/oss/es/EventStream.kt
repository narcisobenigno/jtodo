package todo.oss.es

data class PersistedEventRecord(val event: Event, val streamIds: List<Id>, val position: UInt)

data class EventStream(private val events: List<PersistedEventRecord>) {
    val lastPosition = events.lastOrNull()?.position ?: 1u
}
