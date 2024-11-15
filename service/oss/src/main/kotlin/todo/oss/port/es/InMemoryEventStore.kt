package todo.oss.port.es

import todo.oss.es.ConflictException
import todo.oss.es.EventRecord
import todo.oss.es.EventStream
import todo.oss.es.PersistedEventRecord
import todo.oss.es.StreamQuery

class InMemoryEventStore() {
    private var events = emptyList<PersistedEventRecord>()

    fun append(events: List<EventRecord>, streamQuery: StreamQuery, lastPosition: UInt): Result<String> {
        if (read(streamQuery).lastPosition > lastPosition) {
            return Result.failure(ConflictException(lastPosition))
        }

        val newEvents = events.mapIndexed { i, record ->
            PersistedEventRecord(
                record.event,
                record.streamIds,
                (this.events.size + i + 1).toUInt()
            )
        }
        this.events += newEvents

        return Result.success("successfully stored ${events.size} events")
    }

    fun read(streamQuery: StreamQuery): EventStream {
        return EventStream(this.events.filter {
            it.streamIds.containsAll(streamQuery.streamIds) && streamQuery.eventNames.contains(it.event.eventName)
        })
    }
}