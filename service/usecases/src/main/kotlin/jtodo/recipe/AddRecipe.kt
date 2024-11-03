package jtodo.recipe

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.State
import jtodo.oss.es.Version

data class AddRecipeCommand(val id: Id, val name: String, val ingredients: List<Ingredient>) : Command

sealed class PlanRecipeState : State {
    data object Unplanned : PlanRecipeState()
    data object Planned : PlanRecipeState()
}

class AddRecipe : Decider<AddRecipeCommand, PlanRecipeState> {
    override fun decide(
        command: AddRecipeCommand,
        state: PlanRecipeState,
    ): Result<List<EventRecord>> {
        if (state != PlanRecipeState.Unplanned) {
            return Result.failure(IllegalArgumentException("recipe ${command.id} is already planned"))
        }

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
        record: EventRecord,
    ): PlanRecipeState {
        when (record.event) {
            is RecipeAdded -> return PlanRecipeState.Planned
        }
        return state
    }

    override val initialState = PlanRecipeState.Unplanned
}
