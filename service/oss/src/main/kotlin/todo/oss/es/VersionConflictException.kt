package todo.oss.es

class VersionConflictException(event: EventRecord) :
    Exception("conflict when writing event ${event.event.eventName} version ${event.version}")
