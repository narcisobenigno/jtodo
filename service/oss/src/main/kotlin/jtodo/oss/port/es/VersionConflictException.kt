package jtodo.oss.port.es

import jtodo.oss.es.EventRecord

class VersionConflictException: Exception {
    constructor(event: EventRecord) : super("conflict when writing event ${event.event.eventName} version ${event.version}")
}
