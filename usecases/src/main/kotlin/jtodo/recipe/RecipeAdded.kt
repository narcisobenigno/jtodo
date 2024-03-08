package jtodo.recipe

import jtodo.oss.es.Event
import java.util.UUID

data class RecipeAdded(val id: UUID, val name: String): Event {
    override val eventName: String
        get() = "RecipePlanned"
}
