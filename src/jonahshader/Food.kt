package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CORNER
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

enum class FoodColor {
    RED,
    GREEN,
    BLUE;

    fun getInt() : Int {
        return when (this) {
            RED -> 0
            GREEN -> 1
            BLUE -> 2
        }
    }
}

class Food(world: World, val cellSize: Int) {
    private val FOOD_ACCUMULATE_RATE = 8f
    private val FOOD_ADD_CHANCE = 0.000001f
    private val diffuseChance = 0.0125f
    private val metaFoodAddChanceRatio = 2f


    private val width = world.width / cellSize
    private val height = world.height / cellSize

    private val food = Array(width * height * 3) {0f}

    init {
        for (i in 1..1200)
            run()
    }

    private fun getValue(x: Int, y: Int, color: FoodColor) : Float {
        return food[(x * 3) + (y * 3 * width) + (color.getInt())]
    }

    private fun getValue(x: Int, y: Int, color: Int): Float {
        return food[(x * 3) + (y * 3 * width) + color]
    }

    @Synchronized fun setValue(x: Int, y: Int, color: FoodColor, value: Float) {
        food[(x * 3) + (y * 3 * width) + (color.getInt())] = value
    }

    @Synchronized private fun setValue(x: Int, y: Int, color: Int, value: Float) {
        food[(x * 3) + (y * 3 * width) + color] = value
    }

    private fun diffuse(x: Int, y: Int) {
        val xd = wrap(x + ((Math.random() * -0.5) * 2.5).roundToInt(), width)
        val yd = wrap(y + ((Math.random() * -0.5) * 2.5).roundToInt(), height)

        for (i in 0 until 3) {
            val avg = (getValue(x, y, i) + getValue(xd, yd, i)) / 2.0f
            setValue(x, y, i, avg)
            setValue(xd, yd, i, avg)
        }
    }





    fun readFood(x: Float, y: Float, color: FoodColor) : Float {
        val wrappedY = wrap((y / cellSize).toInt(), height)
        val wrappedX = wrap((x / cellSize).toInt(), width)
        return getValue(wrappedX, wrappedY, color)
    }

    fun readFood(x: Float, y: Float, color: Int) : Float {
        val wrappedY = wrap((y / cellSize).toInt(), height)
        val wrappedX = wrap((x / cellSize).toInt(), width)
        return getValue(wrappedX, wrappedY, color)
    }

    fun eatFood(x: Float, y: Float, color: FoodColor, amount: Float) : Float {
        val wrappedY = wrap((y / cellSize).toInt(), height)
        val wrappedX = wrap((x / cellSize).toInt(), width)

        return if (getValue(wrappedX, wrappedY, color) > amount) {
            setValue(wrappedX, wrappedY, color, getValue(wrappedX, wrappedY, color) - amount)
            amount
        } else {
            val remainingFood = getValue(wrappedX, wrappedY, color)
            setValue(wrappedX, wrappedY, color, 0f)
            remainingFood
        }
    }

    fun run() {
        if (Math.random() < (1 / metaFoodAddChanceRatio)) {
            // add some food to the system then diffuse
            val foodPerTile = FOOD_ACCUMULATE_RATE * cellSize * cellSize
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (Math.random() < FOOD_ADD_CHANCE * metaFoodAddChanceRatio) {
                        setValue(x, y, FoodColor.RED, min(foodPerTile + getValue(x, y, FoodColor.RED), foodPerTile))
                        setValue(x, y, FoodColor.GREEN, min(foodPerTile + getValue(x, y, FoodColor.GREEN), foodPerTile))
                        setValue(x, y, FoodColor.BLUE, min(foodPerTile + getValue(x, y, FoodColor.BLUE), foodPerTile))
                    }
                }
            }
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (Math.random() < diffuseChance)
                diffuse(x, y)
            }
        }
    }

    fun draw(graphics: PApplet) {
        graphics.noStroke()
        graphics.rectMode(CORNER)
        for (y in 0 until height) {
            for (x in 0 until width) {
                graphics.fill((1 - getValue(x, y, FoodColor.RED)) * 255, (1 - getValue(x, y, FoodColor.GREEN)) * 255, (1 - getValue(x, y, FoodColor.BLUE)) * 255)
                graphics.rect((x * cellSize).toFloat(),
                    (y * cellSize).toFloat(),
                    cellSize.toFloat(),
                    cellSize.toFloat())
            }
        }
    }
}
