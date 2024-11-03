package jtodo.recipe

import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.recipe.ingredients.Grams
import jtodo.recipe.ingredients.Ingredient
import jtodo.recipe.planning.PlanRecipe
import jtodo.recipe.planning.RecipePlanned
import jtodo.recipe.planning.RecipePlanning
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test

class RecipePlanningTest {
    @Test
    fun `plans recipe`() {
        val recipePlanning = RecipePlanning()
        val events =
            recipePlanning.decide(
                PlanRecipe(
                    Id.fixed("recipe-1"),
                    "sopa de abobrinha",
                    listOf(
                        Ingredient("abobrinha", Grams(100u)),
                        Ingredient("sal", Grams(8u)),
                    ),
                ),
                recipePlanning.initialState,
            )

        assertEquals(
            Result.success(
                listOf(
                    EventRecord(
                        Id.fixed("recipe-1"),
                        Version(),
                        RecipePlanned(
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

    @Test
    fun `rejects when recipe already added`() {
        val recipePlanning = RecipePlanning()
        val currentState = recipePlanning.evolve(
            recipePlanning.initialState, EventRecord(
                Id.fixed("recipe-1"),
                Version(),
                RecipePlanned(
                    Id.fixed("recipe-1"),
                    "sopa de abobrinha",
                    listOf(
                        Ingredient("abobrinha", Grams(100u)),
                        Ingredient("sal", Grams(8u)),
                    ),
                ),
            )
        )

        val events =
            recipePlanning.decide(
                PlanRecipe(
                    Id.fixed("recipe-1"),
                    "sopa de abobrinha",
                    listOf(
                        Ingredient("abobrinha", Grams(100u)),
                        Ingredient("sal", Grams(8u)),
                        Ingredient("azeite", Grams(10u)),
                    ),
                ),
                currentState,
            )

        assertThrowsExactly(
            IllegalArgumentException::class.java,
            { events.getOrThrow() },
            "recipe ${Id.fixed("recipe-1")} is already planned"
        )
    }
}
