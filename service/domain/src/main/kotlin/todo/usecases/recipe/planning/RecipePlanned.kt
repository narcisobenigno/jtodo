package todo.usecases.recipe.planning

import todo.oss.es.Event
import todo.oss.es.Id
import todo.recipe.ingredients.Ingredient

data class RecipePlanned(val id: Id, val name: String, val listOf: List<Ingredient>) : Event {
    override val eventName: String
        get() = "RecipePlanned"
}
