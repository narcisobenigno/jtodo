package jtodo.oss.es

interface EventStream {
    fun load(id: Id): List<EventRecord>
    fun write(newEvents: List<EventRecord>)
}