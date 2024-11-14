package todo.oss.port.es

import todo.oss.es.VersionedEventRecord
import todo.oss.es.VersionedEventStore
import todo.oss.es.Id
import todo.oss.es.Version
import todo.oss.es.VersionConflictException

class InMemoryVersionedEventStore(vararg events: VersionedEventRecord) : VersionedEventStore {
    private val events = mutableMapOf<Id, MutableMap<Version, VersionedEventRecord>>()

    init {
        write(listOf(*events))
    }

    override fun load(id: Id): List<VersionedEventRecord> {
        return events[id]?.values?.sortedBy(VersionedEventRecord::version) ?: listOf()
    }

    override fun write(newEvents: List<VersionedEventRecord>) {
        newEvents.forEach {
            events[it.id] = events[it.id] ?: mutableMapOf()

            if (events[it.id]?.containsKey(it.version) == true) {
                throw VersionConflictException(it)
            }
        }

        newEvents.forEach { events[it.id]?.set(it.version, it) }
    }
}
