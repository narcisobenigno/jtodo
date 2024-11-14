package todo.usecases.recipe.addingredient

import todo.oss.es.EventRecord
import todo.oss.es.Id
import todo.oss.es.Version
import todo.recipe.ingredients.Grams
import todo.recipe.ingredients.Ingredient
import todo.usecases.recipe.planning.RecipePlanned
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import todo.usecases.recipe.addingredient.AddIngredientToRecipe
import todo.usecases.recipe.addingredient.AddingIngredientToRecipe
import todo.usecases.recipe.addingredient.IngredientAddedToRecipe

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