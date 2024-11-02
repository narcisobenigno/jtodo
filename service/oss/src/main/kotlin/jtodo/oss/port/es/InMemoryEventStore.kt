package jtodo.oss.port.es

import jtodo.oss.es.*

class InMemoryEventStore(vararg events: EventRecord) : EventStore {
    private val events = mutableMapOf<Id, MutableMap<Version, EventRecord>>()

    init {
        write(listOf(*events) )
    }

    override fun load(id: Id): List<EventRecord> {
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