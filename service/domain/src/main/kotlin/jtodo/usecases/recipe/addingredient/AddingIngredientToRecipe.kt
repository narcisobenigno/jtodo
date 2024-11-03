package jtodo.usecases.recipe.addingredient

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.State
import jtodo.oss.es.Version
import jtodo.recipe.ingredients.Ingredient

data class AddIngredientToRecipe(val id: Id, val newIngredient: Ingredient) : Command

sealed class AddIngredientToRecipeState: State {
    data object NotAddedYet: AddIngredientToRecipeState()
}

class AddingIngredientToRecipe : Decider<AddIngredientToRecipe, AddIngredientToRecipeState> {
    override fun decide(command: AddIngredientToRecipe, state: AddIngredientToRecipeState): Result<List<EventRecord>> {
        return Result.success(listOf(
            EventRecord(command.id, Version(2u), IngredientAddedToRecipe(
                command.id,
                command.newIngredient
            ))
        ))
    }

    override fun evolve(state: AddIngredientToRecipeState, record: EventRecord): AddIngredientToRecipeState {
        return state
    }

    override val initialState = AddIngredientToRecipeState.NotAddedYet
}