package jtodo.oss.es

import java.util.UUID

interface EventStream {
    fun load(id: UUID): List<EventEnvelop>
    fun write(events: List<EventEnvelop>)
}