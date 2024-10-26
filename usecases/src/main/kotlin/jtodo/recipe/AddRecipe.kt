package jtodo.recipe

import jtodo.oss.es.*
import java.util.UUID

data class AddRecipeCommand(val id: UUID, val name: String, val ingredients: List<Ingredient>): Command

class PlanRecipeState: State {

}

class AddRecipe: Decider<AddRecipeCommand, PlanRecipeState> {
    override fun decide(command: AddRecipeCommand, state: PlanRecipeState): Result<List<EventRecord>> {
        return Result.success(listOf(
            EventRecord(command.id, 1, RecipeAdded(
                command.id,
                command.name,
                command.ingredients
            ))
        ))
    }

    override fun evolve(state: PlanRecipeState, event: EventRecord): PlanRecipeState {
        TODO("Not yet implemented")
    }

    override val initialState = PlanRecipeState()
}