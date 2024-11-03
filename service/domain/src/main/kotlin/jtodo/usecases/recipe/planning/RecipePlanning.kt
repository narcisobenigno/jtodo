package jtodo.usecases.recipe.planning

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.State
import jtodo.oss.es.Version
import jtodo.recipe.ingredients.Ingredient

data class PlanRecipe(val id: Id, val name: String, val ingredients: List<Ingredient>) : Command

sealed class PlanRecipeState : State {
    data object Unplanned : PlanRecipeState()
    data object Planned : PlanRecipeState()
}

class RecipePlanning : Decider<PlanRecipe, PlanRecipeState> {
    override fun decide(
        command: PlanRecipe,
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
                    RecipePlanned(
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
            is RecipePlanned -> return PlanRecipeState.Planned
        }
        return state
    }

    override val initialState = PlanRecipeState.Unplanned
}
