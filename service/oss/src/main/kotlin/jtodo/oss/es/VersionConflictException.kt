package jtodo.oss.es

import jtodo.oss.es.EventRecord

class VersionConflictException(event: EventRecord) :
    Exception("conflict when writing event ${event.event.eventName} version ${event.version}") {
}
