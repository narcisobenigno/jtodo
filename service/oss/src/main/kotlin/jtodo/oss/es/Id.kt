package jtodo.oss.es

import java.util.UUID

data class Id(private val value: UUID) {
    companion object {
        fun scoped(scope: String, value: String): Id {
            return Id(UUID.nameUUIDFromBytes((scope + value).encodeToByteArray()))
        }
    }
}
