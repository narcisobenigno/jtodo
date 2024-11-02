package jtodo.oss.es

interface EventStore {
    fun load(id: Id): List<EventRecord>

    fun write(newEvents: List<EventRecord>)
}
