package todo.oss.es

sealed class Position {
    data object NotInitialized : Position()
    data class Current(private val value: UInt) : Position() {
        init {
            require(value > 0u)
        }
    }
}
