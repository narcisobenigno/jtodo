package jtodo.recipe

import jtodo.oss.es.Event
import java.util.UUID

data class RecipePlanned(val id: UUID, val name: String): Event {
    override val eventName: String
        get() = "RecipePlanned"
}