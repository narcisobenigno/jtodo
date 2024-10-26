package jtodo.oss.port.es

import jtodo.oss.es.EventRecord
import jtodo.oss.es.EventStream
import java.util.*

class InMemoryEventStream: EventStream {
    private val events = mutableMapOf<UUID, MutableMap<Int, EventRecord>>()

    override fun load(id: UUID): List<EventRecord> {
        return events[id]?.values?.sortedBy(EventRecord::version) ?: listOf()
    }

    override fun write(newEvents: List<EventRecord>) {
        newEvents.forEach {
            events[it.id] = events[it.id] ?: mutableMapOf()

            if (events[it.id]?.containsKey(it.version) == true) {
                    throw VersionConflictException(it)
            }
        }

        newEvents.forEach { events[it.id]?.set(it.version, it) }
    }
}