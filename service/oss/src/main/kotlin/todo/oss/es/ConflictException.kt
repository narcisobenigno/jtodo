package todo.oss.es

data class ConflictException(private val lastPosition: Position) :
    Exception("conflict when writing event with expected position")
