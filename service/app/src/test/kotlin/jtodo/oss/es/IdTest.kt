package jtodo.oss.es

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IdTest {
    @Test
    fun `builds scoped id`() {
        assertEquals(
            Id.scoped("scope", "value"),
            Id.scoped("scope", "value")
        )

        assertNotEquals(
            Id.scoped("scope", "value"),
            Id.scoped("scope", "another-value")
        )
        assertNotEquals(
            Id.scoped("scope", "value"),
            Id.scoped("another-scope", "value")
        )
    }

    @Test
    fun `builds fixed string`() {
        assertEquals(
            Id.fixed("value"),
            Id.fixed("value")
        )
        assertEquals(
            Id.fixed("value"),
            Id.fixed(" value    ")
        )

        assertNotEquals(
            Id.fixed("value"),
            Id.fixed("another value")
        )
        assertThrows<IllegalArgumentException>("value cannot be empty") { Id.fixed("") }
        assertThrows<IllegalArgumentException>("value cannot be empty") { Id.fixed("    ") }
    }
}