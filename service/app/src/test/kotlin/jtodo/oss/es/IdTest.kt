package jtodo.oss.es

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
}