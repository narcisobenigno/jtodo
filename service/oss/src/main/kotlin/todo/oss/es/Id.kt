package todo.oss.es

import java.util.UUID

data class Id(private val value: UUID) {
    constructor(value: String) : this(UUID.fromString(value.trim()))

    companion object {
        fun scoped(
            scope: String,
            value: String,
        ): Id {
            return Id(UUID.nameUUIDFromBytes((scope + value).encodeToByteArray()))
        }

        fun fixed(value: String): Id {
            require(value.trim().isNotEmpty()) { "value cannot be empty" }
            return Id(UUID.nameUUIDFromBytes(value.trim().encodeToByteArray()))
        }
    }
}
