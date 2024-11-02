package jtodo.recipe

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.State
import jtodo.oss.es.Version

data class AddRecipeCommand(val id: Id, val name: String, val ingredients: List<Ingredient>) : Command

class PlanRecipeState : State

class AddRecipe : Decider<AddRecipeCommand, PlanRecipeState> {
    override fun decide(
        command: AddRecipeCommand,
        state: PlanRecipeState,
    ): Result<List<EventRecord>> {
        return Result.success(
            listOf(
                EventRecord(
                    command.id,
                    Version(),
                    RecipeAdded(
                        command.id,
                        command.name,
                        command.ingredients,
                    ),
                ),
            ),
        )
    }

    override fun evolve(
        state: PlanRecipeState,
        event: EventRecord,
    ): PlanRecipeState {
        TODO("Not yet implemented")
    }

    override val initialState = PlanRecipeState()
}
