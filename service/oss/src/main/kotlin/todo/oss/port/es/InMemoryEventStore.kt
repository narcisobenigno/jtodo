package todo.oss.port.es

import todo.oss.es.EventRecord
import todo.oss.es.EventStream
import todo.oss.es.PersistedEventRecord
import todo.oss.es.StreamQuery

class InMemoryEventStore() {
    private var events = emptyList<PersistedEventRecord>()

    fun append(events: List<EventRecord>, streamQuery: StreamQuery, lastPosition: UInt) {
        this.events += events.mapIndexed { i, record ->
            PersistedEventRecord(
                record.event,
                record.streamIds,
                (this.events.size + i + 1).toUInt()
            )
        }
    }

    fun read(streamQuery: StreamQuery): EventStream {
        return EventStream(this.events.filter { it.streamIds.containsAll(streamQuery.streamIds) })
    }
}