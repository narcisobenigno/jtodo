package jtodo.recipe

import jtodo.oss.es.Event
import java.util.UUID

data class RecipeAdded(val id: UUID, val name: String, val listOf: List<Ingredient>): Event {
    override val eventName: String
        get() = "RecipePlanned"
}
