package jtodo.recipe

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventEnvelop
import jtodo.oss.es.State
import java.util.UUID

data class AddRecipeCommand(val id: UUID, val name: String, val ingredients: List<Ingredient>): Command

class PlanRecipeState: State {

}

class AddRecipe: Decider<AddRecipeCommand, PlanRecipeState> {
    override fun decide(command: AddRecipeCommand, state: PlanRecipeState): List<EventEnvelop> {
        return listOf(EventEnvelop(
            command.id,
            1,
            RecipeAdded(
                command.id,
                command.name,
                command.ingredients
            )
        ))
    }

    override fun evolve(state: PlanRecipeState, event: EventEnvelop): PlanRecipeState {
        TODO("Not yet implemented")
    }

    override val initialState = PlanRecipeState()
}