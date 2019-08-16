package jonahshader.datatypes

import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.math.pow

class AngleMagnitude(val angle: Float, val magnitude: Float)

class ComplexNumber(var real: Float, var imaginary: Float) {
    fun multiplyBy(num: ComplexNumber) {
        val newReal = (real * num.real) - (imaginary * num.imaginary)
        val newImaginary = (real * num.imaginary) + (imaginary * num.real)

        real = newReal
        imaginary = newImaginary
    }

    fun toAngleMagnitude() : AngleMagnitude = AngleMagnitude(atan2(imaginary, real), sqrt(real.pow(2) + imaginary.pow(2)))
}