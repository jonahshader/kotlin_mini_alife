package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CENTER
import java.lang.Math.random
import kotlin.math.*

class Creature(private var x: Float, private var y: Float, private val world: World) {
    private val colorVariance = 0.1f
    private val eatColorMutationRate = 0.1f
    private val speedScale = 0.008f
    private val eatRate = 0.006f
    private val foodToMass = 50f
    private val sensorPositionMutationRate = 0.02f
    private val brainAllocatedRecursiveSteps = 8

    private var currentBrainRecursiveStepsRemaining = 8

    private var red = random().toFloat()
    private var green = random().toFloat()
    private var blue = random().toFloat()

    private val density = 50f // kilograms per meter cubed
    private val g = 9.8f // meters per second squared
    private val friction = 0.995f // low values is lower friction. 1.0 is max

    private var mass = 30f // kilograms
    private var size = 0f // diameter

    private var xSpeed = 0f
    private var ySpeed = 0f

    private var redEatRate = 0f
    private var greenEatRate = 0f
    private var blueEatRate = 0f

    private var sensor1XOffset = 0f
    private var sensor1YOffset = 0f
    private var sensor2XOffset = 0f
    private var sensor2YOffset = 0f

    private var brain = NeuralNetwork(9, 6, 12, 4, relu)

    init {
        updateSize()

        redEatRate = Math.random().toFloat()
        greenEatRate = Math.random().toFloat()
        blueEatRate = Math.random().toFloat()
        normalizePowColorEatRates(1.25f)

//        sensor1XOffset = random().toFloat() - 0.5f
//        sensor1YOffset = random().toFloat() - 0.5f
//        sensor2XOffset = random().toFloat() - 0.5f
//        sensor2YOffset = random().toFloat() - 0.5f
    }

    constructor(parent: Creature) : this(parent.x, parent.y, parent.world) {
        brain = NeuralNetwork(parent.brain)
        brain.mutate(world.rand)
        red = mutateValueLimited(parent.red, colorVariance)
        green = mutateValueLimited(parent.green, colorVariance)
        blue = mutateValueLimited(parent.blue, colorVariance)

        redEatRate = mutateValueLimitedGaussian(parent.redEatRate, eatColorMutationRate)
        greenEatRate = mutateValueLimitedGaussian(parent.greenEatRate, eatColorMutationRate)
        blueEatRate = mutateValueLimitedGaussian(parent.blueEatRate, eatColorMutationRate)
        normalizePowColorEatRates(1.25f)

//        sensor1XOffset = mutateValueLimitedGaussian(parent.sensor1XOffset, sensorPositionMutationRate)
//        sensor1YOffset = mutateValueLimitedGaussian(parent.sensor1YOffset, sensorPositionMutationRate)
//        sensor2XOffset = mutateValueLimitedGaussian(parent.sensor2XOffset, sensorPositionMutationRate)
//        sensor2YOffset = mutateValueLimitedGaussian(parent.sensor2YOffset, sensorPositionMutationRate)
    }

    fun run() {
        brain.inputNeurons[0] = xSpeed
        brain.inputNeurons[1] = ySpeed
        brain.inputNeurons[2] = size / 30f

        var updateBrain = false


        for (i in 3..5) {
            val foodSensor = (world.food.readFood(x + sensor1XOffset, y + sensor1YOffset, i - 3) * 2f) - 1f
            if (brain.inputNeurons[i] != foodSensor) {
                brain.inputNeurons[i] = foodSensor
                currentBrainRecursiveStepsRemaining = brainAllocatedRecursiveSteps // reset recursive brain step timer
            }
        }
        for (i in 6..8) {
            val foodSensor = (world.food.readFood(x + sensor1XOffset, y + sensor1YOffset, i - 6) * 2f) - 1f
            if (brain.inputNeurons[i] != foodSensor) {
                brain.inputNeurons[i] = foodSensor
                currentBrainRecursiveStepsRemaining = brainAllocatedRecursiveSteps
            }
        }
//        for (i in 8..10)
//            brain.inputNeurons[i] = world.food.readFood(x + 1, y, i - 8) * 2f
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


        if (currentBrainRecursiveStepsRemaining > 0) {
            brain.calculateOutputs()
            currentBrainRecursiveStepsRemaining--
        }

        xSpeed += (brain.outputNeurons[0] * speedScale - xSpeed) * friction.pow(mass / 10)
        ySpeed += (brain.outputNeurons[1] * speedScale - ySpeed) * friction.pow(mass / 10)

        x += xSpeed
        y += ySpeed

        x = wrap(x, world.width.toFloat())
        y = wrap(y, world.height.toFloat())

        mass += world.food.eatFood(x, y, FoodColor.RED, eatRate * redEatRate) * foodToMass
        mass += world.food.eatFood(x, y, FoodColor.GREEN, eatRate * greenEatRate) * foodToMass
        mass += world.food.eatFood(x, y, FoodColor.BLUE, eatRate * blueEatRate) * foodToMass

        sensor1XOffset *= 0.9f
        sensor1YOffset *= 0.9f
        sensor2XOffset *= 0.9f
        sensor2YOffset *= 0.9f

        sensor1XOffset += brain.outputNeurons[2] * .01f
        sensor1YOffset += brain.outputNeurons[3] * .01f
        sensor2XOffset += brain.outputNeurons[4] * .01f
        sensor2YOffset += brain.outputNeurons[5] * .01f

        if (mass > 36) {
            world.creatures.addQueued(Creature(this))
//            world.creatures.addQueued(Creature(this))
//            world.creatures.addQueued(Creature(this))
//            world.creatures.addQueued(Creature(this))
            mass -= 10
//            world.creatures.removeQueued(this)
        } else if (mass < 0) {
            world.creatures.removeQueued(this)
        }

        mass -= 0.025f
        //println(mass)
        updateSize()
    }

    fun draw(graphics: PApplet) {
        graphics.ellipseMode(CENTER)
        graphics.noStroke()
//        graphics.fill(red * 255, green * 255, blue * 255)
        graphics.fill(redEatRate * 255, greenEatRate * 255, blueEatRate * 255)
        graphics.ellipse(x, y, size, size)

//        graphics.stroke(0)
//        graphics.strokeWeight(1f/2f)
//        val worldToPixel = graphics.pixelWidth / world.width.toFloat()
//        graphics.pixels[wrap((x + sensor1XOffset) * worldToPixel, graphics.pixelWidth.toFloat()).toInt() +
//                (wrap((y + sensor1YOffset) * worldToPixel, graphics.pixelWidth.toFloat()).toInt() * graphics.pixelWidth)] = 0
//        graphics.pixels[wrap((x + sensor2XOffset) * worldToPixel, graphics.pixelWidth.toFloat()).toInt() +
//                (wrap((y + sensor2YOffset) * worldToPixel, graphics.pixelWidth.toFloat()).toInt() * graphics.pixelWidth)] = 0
        graphics.fill(0)
        graphics.ellipse(x + sensor1XOffset, y + sensor1YOffset, 0.5f * size, 0.5f * size)
        graphics.ellipse(x + sensor2XOffset, y + sensor2YOffset, 0.5f * size, 0.5f * size)
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