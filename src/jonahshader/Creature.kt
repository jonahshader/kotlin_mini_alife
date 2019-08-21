package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CENTER
import java.lang.Math.random
import kotlin.math.*

class Creature(private var x: Float, private var y: Float, private val world: World) {
    private val colorVariance = 0.1f
    private val eatColorMutationRate = 0.1f
    private val speedScale = 0.008f
    private val eatRate = 0.001f
    private val foodToMass = 100f

    private var red = random().toFloat()
    private var green = random().toFloat()
    private var blue = random().toFloat()

    private val density = 50f // kilograms per meter cubed
    private val g = 9.8f // meters per second squared
    private val friction = 0.99f // low values is lower friction. 1.0 is max

    private var mass = 30f // kilograms
    private var size = 0f // diameter

    private var xSpeed = 0f
    private var ySpeed = 0f

    private var redEatRate = 0f
    private var greenEatRate = 0f
    private var blueEatRate = 0f

    private var brain = NeuralNetwork(5, 5, 4, tanh)

    init {
        updateSize()

        redEatRate = Math.random().toFloat()
        greenEatRate = Math.random().toFloat()
        blueEatRate = Math.random().toFloat()
        normalizePowColorEatRates(1.25f)
    }


    fun run() {
        brain.inputNeurons[0] = xSpeed
        brain.inputNeurons[1] = ySpeed
        brain.inputNeurons[2] = world.food.readFood(x, y, FoodColor.RED) * 2f
        brain.inputNeurons[3] = world.food.readFood(x, y, FoodColor.GREEN) * 2f
        brain.inputNeurons[4] = world.food.readFood(x, y, FoodColor.BLUE) * 2f
//        brain.inputNeurons[0] = world.food.readFood(x - 1f, y, FoodColor.RED) * 2f
//        brain.inputNeurons[1] = world.food.readFood(x + 1f, y, FoodColor.RED) * 2f
//        brain.inputNeurons[2] = world.food.readFood(x, y - 1f, FoodColor.RED) * 2f
//        brain.inputNeurons[3] = world.food.readFood(x, y + 1f, FoodColor.RED) * 2f
//
//        brain.inputNeurons[4] = world.food.readFood(x - 1f, y, FoodColor.GREEN) * 2f
//        brain.inputNeurons[5] = world.food.readFood(x + 1f, y, FoodColor.GREEN) * 2f
//        brain.inputNeurons[6] = world.food.readFood(x, y - 1f, FoodColor.GREEN) * 2f
//        brain.inputNeurons[7] = world.food.readFood(x, y + 1f, FoodColor.GREEN) * 2f
//
//        brain.inputNeurons[8] = world.food.readFood(x - 1f, y, FoodColor.BLUE) * 2f
//        brain.inputNeurons[9] = world.food.readFood(x + 1f, y, FoodColor.BLUE) * 2f
//        brain.inputNeurons[10] = world.food.readFood(x, y - 1f, FoodColor.BLUE) * 2f
//        brain.inputNeurons[11] = world.food.readFood(x, y + 1f, FoodColor.BLUE) * 2f

        brain.calculateOutputs()
        xSpeed += (brain.outputNeurons[0] * speedScale - xSpeed) * friction.pow(mass / 10)
        ySpeed += (brain.outputNeurons[1] * speedScale - ySpeed) * friction.pow(mass / 10)

        x += xSpeed
        y += ySpeed

        x = wrap(x, world.width.toFloat())
        y = wrap(y, world.height.toFloat())

        mass += world.food.eatFood(x, y, FoodColor.RED, eatRate * redEatRate) * foodToMass
        mass += world.food.eatFood(x, y, FoodColor.GREEN, eatRate * greenEatRate) * foodToMass
        mass += world.food.eatFood(x, y, FoodColor.BLUE, eatRate * blueEatRate) * foodToMass

        if (mass > 35) {
            world.creatures.addQueued(Creature(this))
            world.creatures.addQueued(Creature(this))
//            mass -= 20
            world.creatures.removeQueued(this)
        } else if (mass < 0) {
            world.creatures.removeQueued(this)
        }

        mass -= 0.02f
        //println(mass)
        updateSize()
    }

    constructor(parent: Creature) : this(parent.x, parent.y, parent.world) {
        brain = NeuralNetwork(parent.brain)
        brain.mutate()
        red = mutateValueLimited(parent.red, colorVariance)
        green = mutateValueLimited(parent.green, colorVariance)
        blue = mutateValueLimited(parent.blue, colorVariance)

        redEatRate = mutateValueLimitedGaussian(parent.redEatRate, eatColorMutationRate)
        greenEatRate = mutateValueLimitedGaussian(parent.greenEatRate, eatColorMutationRate)
        blueEatRate = mutateValueLimitedGaussian(parent.blueEatRate, eatColorMutationRate)
        normalizePowColorEatRates(1.25f)
    }

    fun draw(graphics: PApplet) {
        graphics.ellipseMode(CENTER)
        graphics.noStroke()
//        graphics.fill(red * 255, green * 255, blue * 255)
        graphics.fill(redEatRate * 255, greenEatRate * 255, blueEatRate * 255)
        graphics.ellipse(x, y, size, size)
    }


    private fun updateSize() {
        val volume = mass / density
        size = 2f * (volume * 3f / (4f * PI.toFloat())).pow(1/3f)
    }

    private fun mutateValueLimited(originalValue: Float, variance: Float) : Float {
        val c = originalValue + (Math.random().toFloat() - 0.5f) * 2.0f * variance
        return min(max(0f, c), 1f)
    }

    private fun mutateValueLimitedGaussian(originalValue: Float, variance: Float) : Float {
        val c = originalValue + world.rand.nextGaussian().toFloat() * variance
        return min(max(0f, c), 1f)
    }

    private fun normalizePowColorEatRates(pow: Float) {
        val sum = redEatRate + greenEatRate + blueEatRate
        redEatRate /= sum
        greenEatRate /= sum
        blueEatRate /= sum

        redEatRate = redEatRate.pow(pow)
        greenEatRate = greenEatRate.pow(pow)
        blueEatRate = blueEatRate.pow(pow)

        redEatRate = max(redEatRate, 0f)
        greenEatRate = max(greenEatRate, 0f)
        blueEatRate = max(blueEatRate, 0f)
    }
}