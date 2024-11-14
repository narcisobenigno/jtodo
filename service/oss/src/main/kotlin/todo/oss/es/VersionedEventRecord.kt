package todo.oss.es

data class VersionedEventRecord(val id: Id, val version: Version, val event: Event)