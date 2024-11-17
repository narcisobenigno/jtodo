package todo.oss.port.es

import todo.oss.es.ConflictException
import todo.oss.es.EventRecord
import todo.oss.es.EventStore
import todo.oss.es.EventStream
import todo.oss.es.PersistedEventRecord
import todo.oss.es.Position
import todo.oss.es.StreamQuery

class InMemoryEventStore : EventStore {
    private var events = emptyList<PersistedEventRecord>()

    override fun append(events: List<EventRecord>, streamQuery: StreamQuery, lastPosition: Position): Result<String> {
        if (this.read(streamQuery).lastPosition != lastPosition) {
            return Result.failure(ConflictException(lastPosition))
        }

        this.events += events.mapIndexed { i, record ->
            PersistedEventRecord(
                record.event,
                record.streamIds,
                Position.Current((this.events.size + i + 1).toUInt())
            )
        }

        return Result.success("successfully stored ${events.size} events")
    }

    override fun read(streamQuery: StreamQuery): EventStream {
        return EventStream(
            this.events
                .filter { it.streamIds.intersect(streamQuery.streamIds).isNotEmpty() }
                .filter { streamQuery.eventNames.contains(it.event.eventName) }
        )
    }
}