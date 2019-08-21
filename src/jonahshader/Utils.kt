package jonahshader

fun wrap(value: Float, limit: Float) : Float {
    val remainder = value.rem(limit)
    return when {
        remainder < 0 -> remainder + limit
        remainder == limit -> 0f
        else -> remainder
    }
}

fun wrap(value: Int, limit: Int) : Int {
    val remainder = value.rem(limit)
    return when {
        remainder < 0 -> remainder + limit
        remainder == limit -> 0
        else -> remainder
    }
}