package todo.usecases.recipe.planning

import todo.oss.es.VersionedEventRecord
import todo.oss.es.Id
import todo.oss.es.Version
import todo.recipe.ingredients.Grams
import todo.recipe.ingredients.Ingredient
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
                    VersionedEventRecord(
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
            recipePlanning.initialState, VersionedEventRecord(
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
