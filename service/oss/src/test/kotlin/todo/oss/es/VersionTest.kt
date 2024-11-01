package todo.oss.es

import jtodo.oss.es.Version
import org.junit.jupiter.api.Assertions.*
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

    @Test
    fun `bumps version`() {
        assertEquals(Version().bump(), Version(2u))
        assertEquals(Version(2u).bump(), Version(3u))
    }

    @Test
    fun `compares `() {
        assertTrue(Version(1u) == (Version(1u)))
        assertTrue(Version(1u) < Version(3u))
        assertTrue(Version(4u) > Version(1u))
    }
}