package todo.oss.es

import jtodo.oss.es.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class VersionTest {
    @Test
    fun `identity check`() {
        assertEquals(Version(1u), Version(1u))
        assertEquals(Version(1u), Version())
        assertNotEquals(Version(1u), Version(2u))
        assertThrows<IllegalArgumentException> {
            Version(0u)
        }
    }
}