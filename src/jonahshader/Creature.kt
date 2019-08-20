package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CENTER
import java.awt.Color
import java.lang.Math.cbrt
import java.lang.Math.random
import kotlin.math.*

class Creature(var x: Float, var y: Float, val world: World) {
    private val speedScale = 0.008f
    private val eatRate = 0.001f
    private val foodToMass = 100f
    private var color = Color(random().toFloat(), random().toFloat(), random().toFloat())

    private val density = 50f // kilograms per meter cubed
    private val g = 9.8f // meters per second squared
    private val friction = 0.99f // low values is lower friction. 1.0 is max

    private var mass = 30f // kilograms
    private var size = 0f // diameter

    private var xSpeed = 0f
    private var ySpeed = 0f

    init {
        updateSize()
    }

    private var brain = NeuralNetwork(4, 2, 10, relu)

    fun run() {
        brain.inputNeurons[0] = world.food.readFood(x - 1f, y) * 2f
        brain.inputNeurons[1] = world.food.readFood(x + 1f, y) * 2f
        brain.inputNeurons[2] = world.food.readFood(x, y - 1f) * 2f
        brain.inputNeurons[3] = world.food.readFood(x, y + 1f) * 2f

        brain.calculateOutputs()
        xSpeed += (brain.outputNeurons[0] * speedScale - xSpeed) * friction.pow(mass / 10)
        ySpeed += (brain.outputNeurons[1] * speedScale - ySpeed) * friction.pow(mass / 10)

        x += xSpeed
        y += ySpeed

        x = wrap(x, world.width.toFloat())
        y = wrap(y, world.height.toFloat())

        mass += world.food.eatFood(x, y, eatRate) * foodToMass
        if (mass > 35) {
            world.creatures.addQueued(Creature(this))
            world.creatures.addQueued(Creature(this))
            mass -= 20
//            world.creatures.removeQueued(this)
        } else if (mass < 0) {
            world.creatures.removeQueued(this)
        }

        mass -= 0.05f
        //println(mass)
        updateSize()
    }

    constructor(parent: Creature) : this(parent.x, parent.y, parent.world) {
        brain = NeuralNetwork(parent.brain)
        brain.mutate()
        color = Color(parent.color.rgb)
    }

    fun draw(graphics: PApplet) {
        graphics.ellipseMode(CENTER)
        graphics.noStroke()
        graphics.fill(color.rgb)
        graphics.ellipse(x, y, size, size)
    }


    private fun updateSize() {
        val volume = mass / density
        size = 2f * (volume * 3f / (4f * PI.toFloat())).pow(1/3f)
    }
}