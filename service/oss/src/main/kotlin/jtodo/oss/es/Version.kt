package jtodo.oss.es


data class Version(private val value: UInt): Comparable<Version> {
    constructor():this(1u)

    fun bump(): Version {
        return Version(value + 1u)
    }
    init {
        require(value > 0u)
    }

    override fun compareTo(other: Version): Int {
        return value.compareTo(other.value)
    }
}
