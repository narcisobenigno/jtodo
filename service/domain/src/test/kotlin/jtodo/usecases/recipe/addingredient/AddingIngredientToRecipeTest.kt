package jtodo.usecases.recipe.addingredient

import jtodo.oss.es.EventRecord
import jtodo.oss.es.Id
import jtodo.oss.es.Version
import jtodo.recipe.ingredients.Grams
import jtodo.recipe.ingredients.Ingredient
import jtodo.usecases.recipe.planning.RecipePlanned
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AddingIngredientToRecipeTest {
    @Test
    fun `adds new ingredient to recipe`() {
        val subject = AddingIngredientToRecipe()
        val newState = subject.evolve(
            subject.initialState, EventRecord(
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

        assertEquals(
            Result.success(listOf(
                EventRecord(
                    Id.fixed("recipe-1"),
                    Version(2u),
                    IngredientAddedToRecipe(Id.fixed("recipe-1"), Ingredient("azeite", Grams(10u)))
                )
            )),
            subject.decide(
                AddIngredientToRecipe(
                    Id.fixed("recipe-1"),
                    Ingredient("azeite", Grams(10u))
                ),
                newState
            )
        )
    }
}