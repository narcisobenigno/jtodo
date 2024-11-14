package todo.oss.es

class VersionConflictException(event: VersionedEventRecord) :
    Exception("conflict when writing event ${event.event.eventName} version ${event.version}")
