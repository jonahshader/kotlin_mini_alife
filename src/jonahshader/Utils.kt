package jonahshader

fun wrap(value: Float, limit: Float) : Float {
    val remainder = value.rem(limit)
    return if (remainder < 0) {
        remainder + limit
    } else {
        remainder
    }
}

fun wrap(value: Int, limit: Int) : Int {
    val remainder = value.rem(limit)
    return if (remainder < 0) {
        remainder + limit
    } else {
        remainder
    }
}