package jtodo.oss.es

import java.util.UUID

interface EventStream {
    fun load(id: UUID): List<EventRecord>
    fun write(newEvents: List<EventRecord>)
}