package jtodo.recipe.planning

import jtodo.oss.es.Event
import jtodo.oss.es.Id
import jtodo.recipe.ingredients.Ingredient

data class RecipePlanned(val id: Id, val name: String, val listOf: List<Ingredient>) : Event {
    override val eventName: String
        get() = "RecipePlanned"
}
