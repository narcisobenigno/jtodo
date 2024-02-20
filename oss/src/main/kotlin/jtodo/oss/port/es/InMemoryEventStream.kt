package jtodo.oss.port.es

import jtodo.oss.es.EventEnvelop
import jtodo.oss.es.EventStream
import java.util.*

class InMemoryEventStream: EventStream {
    private val events = mutableListOf<EventEnvelop>()

    override fun load(id: UUID): List<EventEnvelop> {
        return events
                .filter { eventEnvelop: EventEnvelop -> eventEnvelop.id == id }
                .sortedBy(EventEnvelop::version)
    }

    override fun write(newEvents: List<EventEnvelop>) {
        newEvents.forEach() { new: EventEnvelop ->
            events.forEach { existing: EventEnvelop ->
                if(new.id == existing.id)
                    throw VersionConflictException(new)
            }
        }

        events.addAll(newEvents)
    }
}