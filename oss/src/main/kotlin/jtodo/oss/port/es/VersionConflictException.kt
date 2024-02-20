package jtodo.oss.port.es

import jtodo.oss.es.Event
import jtodo.oss.es.EventEnvelop

class VersionConflictException: Exception {
    constructor(event: EventEnvelop) : super("conflict when writing event ${event.event.eventName} version ${event.version}")
}
