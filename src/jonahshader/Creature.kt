package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CENTER
import java.lang.Math.cbrt
import java.lang.Math.random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Creature(var x: Float, var y: Float, val world: World) {
    private val density = 997f // kilograms per meter cubed
    private val g = 9.8f // meters per second squared

    private var mass = 50f // kilograms
    private var size = 0f // diameter
    private var normalForce = 0f // newtons from gravity

    private var xSpeed = 0f
    private var ySpeed = 0f

    private val brain = NeuralNetwork(4, 2, 10, relu)

    fun run() {
        var xForce =
    }

    fun draw(graphics: PApplet) {
        graphics.ellipseMode(CENTER)
        graphics.noStroke()
        graphics.fill(0)
        graphics.ellipse(x, y, size, size)
    }


    private fun updateSize() {
        val volume = mass / density
        size = 2f * (volume * 3f / (4f * PI.toFloat())).pow(1/3f)
    }

    private fun updateNormalForce() {
        normalForce = mass * g
    }

}