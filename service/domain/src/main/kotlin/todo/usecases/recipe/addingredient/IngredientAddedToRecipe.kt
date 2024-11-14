package todo.usecases.recipe.addingredient

import todo.oss.es.Event
import todo.oss.es.Id
import todo.recipe.ingredients.Ingredient

data class IngredientAddedToRecipe(val id: Id, val newIngredient: Ingredient): Event {
    override val eventName: String
        get() = "IngredientAddedToRecipe"
}
