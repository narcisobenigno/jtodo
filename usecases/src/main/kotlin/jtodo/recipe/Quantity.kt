package jtodo.recipe

sealed interface Quantity {
    val value: UInt
    val measurement: String
}

data class Grams(override val value: UInt): Quantity {
    override val measurement = "grams"

    init {
        require(value > 0u) {
            "grams has to be greater than zero"
        }
    }
}