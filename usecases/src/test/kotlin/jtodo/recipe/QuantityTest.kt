package jtodo.recipe

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class QuantityTest {
    @Test
    fun equality() {
        assertEquals(Grams(10u), Grams(10u))
        assertNotEquals(Grams(10u), Grams(20u))
    }

    @Test
    fun `grams cannot be zero`() {
        val thrown = assertThrows<IllegalArgumentException> {
            Grams(0u)
        }

        assertEquals("grams has to be greater than zero", thrown.message)
    }
}