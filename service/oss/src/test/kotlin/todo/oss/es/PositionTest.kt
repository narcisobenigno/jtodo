package todo.oss.es

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun equality() {
        assertEquals(Position.Current(1u), Position.Current(1u))
        assertNotEquals(Position.Current(1u), Position.Current(2u))

        assertThrowsExactly(IllegalArgumentException::class.java, {Position.Current(0u)})
    }
}