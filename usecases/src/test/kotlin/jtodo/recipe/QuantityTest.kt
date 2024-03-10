package jtodo.recipe

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class QuantityTest {
    @Nested
    inner class grams {
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

    @Nested
    inner class teaspoon {
        @Test
        fun equality() {
            assertEquals(TeaSpoon(10u), TeaSpoon(10u))
            assertNotEquals(TeaSpoon(10u), TeaSpoon(20u))
        }

        @Test
        fun `grams cannot be zero`() {
            val thrown = assertThrows<IllegalArgumentException> {
                TeaSpoon(0u)
            }

            assertEquals("teaspoon has to be greater than zero", thrown.message)
        }
    }
}