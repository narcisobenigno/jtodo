package jtodo.recipe

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IngredientTest {
    @Test
    fun equality() {
        assertEquals(
            Ingredient("a name", Grams(2u)),
            Ingredient("a name", Grams(2u))
        )
        assertEquals(
            Ingredient(" a name ", Grams(2u)),
            Ingredient("a name", Grams(2u))
        )
        assertNotEquals(
            Ingredient("another name", Grams(2u)),
            Ingredient("a name", Grams(2u))
        )
        assertNotEquals(
            Ingredient("a name", Grams(2u)),
            Ingredient("a name", Grams(3u))
        )
    }

    @Test
    fun `requires a name`() {
        val thrown = assertThrows<IllegalArgumentException> {
            Ingredient("  ", Grams(2u))
        }

        assertEquals("ingredient name is required", thrown.message)
    }
}