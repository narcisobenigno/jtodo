package todo.oss.es

import jtodo.oss.es.Id
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class IdTest {

    @Test
    fun `builds with string or UUID`() {
        assertEquals(
            Id(UUID.fromString("630a2b09-e0cf-4e77-b595-4e934ac35276")),
            Id("630a2b09-e0cf-4e77-b595-4e934ac35276")
        )
        assertEquals(
            Id(UUID.fromString("630a2b09-e0cf-4e77-b595-4e934ac35276")),
            Id(" 630a2b09-e0cf-4e77-b595-4e934ac35276   ")
        )

        assertNotEquals(
            Id(UUID.fromString("630a2b09-e0cf-4e77-b595-4e934ac35276")),
            Id("730a2b09-e0cf-4e77-b595-4e934ac35276")
        )
    }

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