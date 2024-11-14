package todo.usecases.recipe.planning

import todo.oss.es.Command
import todo.oss.es.Decider
import todo.oss.es.VersionedEventRecord
import todo.oss.es.Id
import todo.oss.es.State
import todo.oss.es.Version
import todo.recipe.ingredients.Ingredient

data class PlanRecipe(val id: Id, val name: String, val ingredients: List<Ingredient>) : Command

sealed class PlanRecipeState : State {
    data object Unplanned : PlanRecipeState()
    data object Planned : PlanRecipeState()
}

class RecipePlanning : Decider<PlanRecipe, PlanRecipeState> {
    override fun decide(
        command: PlanRecipe,
        state: PlanRecipeState,
    ): Result<List<VersionedEventRecord>> {
        if (state != PlanRecipeState.Unplanned) {
            return Result.failure(IllegalArgumentException("recipe ${command.id} is already planned"))
        }

        return Result.success(
            listOf(
                VersionedEventRecord(
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
        record: VersionedEventRecord,
    ): PlanRecipeState {
        when (record.event) {
            is RecipePlanned -> return PlanRecipeState.Planned
        }
        return state
    }

    override val initialState = PlanRecipeState.Unplanned
}
