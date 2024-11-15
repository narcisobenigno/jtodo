package todo.oss.es

data class EventRecord(val event: Event, val streamIds: List<Id>)