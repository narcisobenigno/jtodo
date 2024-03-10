package jtodo.recipe

import jtodo.oss.es.EventEnvelop
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class AddRecipeTest {
    @Test
    fun `plans recipe`() {
        val addRecipe = AddRecipe()
        val events = addRecipe.decide(
            AddRecipeCommand(
                UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"),
                "sopa de abobrinha",
                listOf(
                    Ingredient("abobrinha", Grams(100u)),
                    Ingredient("sal", Grams(8u))
                ),
            ),
            addRecipe.initialState
        )

        assertEquals(
            listOf(
                EventEnvelop(
                    UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"),
                    1,
                    RecipeAdded(
                        UUID.fromString("7d1b5333-5097-406c-81f6-847c02ccd140"),
                        "sopa de abobrinha",
                        listOf(
                            Ingredient("abobrinha", Grams(100u)),
                            Ingredient("sal", Grams(8u))
                        ),
                    )
                )
            ),
            events
        )
    }
}