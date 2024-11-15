package todo.oss.es

data class ConflictException(private val lastPosition: UInt) :
    Exception("conflict when writing event with expected position $lastPosition")
