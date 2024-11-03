package jtodo.usecases.recipe.addingredient

import jtodo.oss.es.Event
import jtodo.oss.es.Id
import jtodo.recipe.ingredients.Ingredient

data class IngredientAddedToRecipe(val id: Id, val newIngredient: Ingredient): Event {
    override val eventName: String
        get() = "IngredientAddedToRecipe"
}
