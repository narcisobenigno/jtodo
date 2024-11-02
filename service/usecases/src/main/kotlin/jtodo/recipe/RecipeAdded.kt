package jtodo.recipe

import jtodo.oss.es.Event
import jtodo.oss.es.Id

data class RecipeAdded(val id: Id, val name: String, val listOf: List<Ingredient>) : Event {
    override val eventName: String
        get() = "RecipePlanned"
}
