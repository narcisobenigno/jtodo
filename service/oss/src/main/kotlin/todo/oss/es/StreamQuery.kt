package todo.oss.es

data class StreamQuery(val streamIds: Set<Id>, val eventNames: Set<String>)
