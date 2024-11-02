package jtodo.recipe

import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AddRecipeTest {
    @Test
    fun `plans recipe`() {
        val addRecipe = AddRecipe()
        val events =
            addRecipe.decide(
                AddRecipeCommand(
                    Id.fixed("recipe-1"),
                    "sopa de abobrinha",
                    listOf(
                        Ingredient("abobrinha", Grams(100u)),
                        Ingredient("sal", Grams(8u)),
                    ),
                ),
                addRecipe.initialState,
            )

        assertEquals(
            Result.success(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        Version(),
                        RecipeAdded(
                            Id.fixed("recipe-1"),
                            "sopa de abobrinha",
                            listOf(
                                Ingredient("abobrinha", Grams(100u)),
                                Ingredient("sal", Grams(8u)),
                            ),
                        ),
                    ),
                ),
            ),
            events,
        )
    }
}
