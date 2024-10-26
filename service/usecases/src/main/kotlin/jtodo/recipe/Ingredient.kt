package jtodo.recipe

data class Ingredient(private var name: String, private val quantity: Quantity) {
    init {
        require(name.trim().isNotEmpty()) {
            "ingredient name is required"
        }
        name = name.trim()
    }
}
