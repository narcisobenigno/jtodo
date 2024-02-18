package jtodo.oss.port.es

import jtodo.oss.es.EventEnvelop
import jtodo.oss.es.EventStream
import java.util.*

class InMemoryEventStream: EventStream {
    private val events = mutableListOf<EventEnvelop>()

    override fun load(id: UUID): List<EventEnvelop> {
        return events
    }

    override fun write(newEvents: List<EventEnvelop>) {
        events.addAll(newEvents)
    }
}