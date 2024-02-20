package jtodo.oss.port.es

import jtodo.oss.es.EventEnvelop
import jtodo.oss.es.EventStream
import java.util.*

class InMemoryEventStream: EventStream {
    private val levents = mutableListOf<EventEnvelop>()
    private val events = mutableMapOf<UUID, MutableMap<Int, EventEnvelop>>()

    override fun load(id: UUID): List<EventEnvelop> {
        return events[id]?.values?.sortedBy(EventEnvelop::version) ?: listOf()
    }

    override fun write(newEvents: List<EventEnvelop>) {
        newEvents.forEach { new: EventEnvelop ->
            events[new.id] = events[new.id] ?: mutableMapOf()

            if (events[new.id]?.containsKey(new.version) == true) {
                    throw VersionConflictException(new)
            }
        }

        newEvents.forEach { newEvent: EventEnvelop ->
            events[newEvent.id]?.set(newEvent.version, newEvent)
        }
    }
}