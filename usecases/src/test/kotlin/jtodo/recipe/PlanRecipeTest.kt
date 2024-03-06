package jtodo.recipe

import jtodo.oss.es.EventEnvelop
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Month
import java.util.*

class PlanRecipeTest {
    @Test
    fun `plans recipe`() {
        val planRecipe = PlanRecipe()
        val events = planRecipe.decide(
            PlanRecipeCommand(UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"), "sopa de abobrinha"),
            planRecipe.initialState
        )

        assertEquals(
            listOf(
                EventEnvelop(
                    UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"),
                    1,
                    RecipePlanned(
                        UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"),
                        "sopa de abobrinha"
                    )
                )
            ),
            events
        )
    }
}