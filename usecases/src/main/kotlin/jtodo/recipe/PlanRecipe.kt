package jtodo.recipe

import jtodo.oss.es.Command
import jtodo.oss.es.Decider
import jtodo.oss.es.EventEnvelop
import jtodo.oss.es.State
import java.util.UUID

data class PlanRecipeCommand(val id: UUID, val name: String): Command

class PlanRecipeState: State {

}

class PlanRecipe: Decider<PlanRecipeCommand, PlanRecipeState> {
    override fun decide(command: PlanRecipeCommand, state: PlanRecipeState): List<EventEnvelop> {
        return listOf(EventEnvelop(
            command.id,
            1,
            RecipePlanned(
                command.id,
                command.name
            )
        ))
    }

    override fun evolve(state: PlanRecipeState, event: EventEnvelop): PlanRecipeState {
        TODO("Not yet implemented")
    }

    override val initialState = PlanRecipeState()
}