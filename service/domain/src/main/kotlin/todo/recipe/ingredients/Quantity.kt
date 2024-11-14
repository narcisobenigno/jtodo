package todo.recipe.ingredients

sealed interface Quantity {
    val value: UInt
    val measurement: String
}

data class Grams(override val value: UInt) : Quantity {
    override val measurement = "grams"

    init {
        require(value > 0u) {
            "grams has to be greater than zero"
        }
    }
}

data class TeaSpoon(override val value: UInt) : Quantity {
    override val measurement = "teaspoon"

    init {
        require(value > 0u) {
            "teaspoon has to be greater than zero"
        }
    }
}

data class TableSpoon(override val value: UInt) : Quantity {
    override val measurement = "tablespoon"

    init {
        require(value > 0u) {
            "tablespoon has to be greater than zero"
        }
    }
}
