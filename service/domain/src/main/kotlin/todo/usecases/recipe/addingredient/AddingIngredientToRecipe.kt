package todo.usecases.recipe.addingredient

import todo.oss.es.Command
import todo.oss.es.Decider
import todo.oss.es.VersionedEventRecord
import todo.oss.es.Id
import todo.oss.es.State
import todo.oss.es.Version
import todo.recipe.ingredients.Ingredient

data class AddIngredientToRecipe(val id: Id, val newIngredient: Ingredient) : Command

sealed class AddIngredientToRecipeState: State {
    data object NotAddedYet: AddIngredientToRecipeState()
}

class AddingIngredientToRecipe : Decider<AddIngredientToRecipe, AddIngredientToRecipeState> {
    override fun decide(command: AddIngredientToRecipe, state: AddIngredientToRecipeState): Result<List<VersionedEventRecord>> {
        return Result.success(listOf(
            VersionedEventRecord(command.id, Version(2u), IngredientAddedToRecipe(
                command.id,
                command.newIngredient
            )
            )
        ))
    }

    override fun evolve(state: AddIngredientToRecipeState, record: VersionedEventRecord): AddIngredientToRecipeState {
        return state
    }

    override val initialState = AddIngredientToRecipeState.NotAddedYet
}