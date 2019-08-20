package jonahshader

import processing.core.PApplet
import processing.core.PConstants.CORNER
import kotlin.math.pow

class Food(world: World, val cellSize: Int) {
    private val FOOD_ACCUMULATE_RATE = 0.15f
    private val FOOD_ADD_CHANCE = 0.0001f
    private val FOOD_DIFFUSE_CHANCE = 0.1f


    private val food = ArrayList<ArrayList<Float>>()
    private val width = world.width / cellSize
    private val height = world.height / cellSize

    init {
        for (y in 0 until height) {
            food.add(ArrayList())
            for (x in 0 until width) {
                food[y].add(0f)
            }
        }
    }

    fun readFood(x: Float, y: Float) : Float {
        var wrappedY = wrap((y / cellSize).toInt(), height)
        var wrappedX = wrap((x / cellSize).toInt(), width)
        return food[wrappedY][wrappedX]
    }
    private fun writeFood(x: Float, y: Float, value: Float) {
        food[(y / cellSize).toInt()][(x / cellSize).toInt()] = value
    }
    fun eatFood(x: Float, y: Float, amount: Float) : Float {
        return if (readFood(x, y) > amount) {
            writeFood(x, y, readFood(x, y) - amount)
            amount
        } else {
            val remainingFood = readFood(x, y)
            writeFood(x, y, 0f)
            remainingFood
        }
    }

    fun run() {
        // add some food to the system then diffuse
        val foodPerTile = FOOD_ACCUMULATE_RATE * cellSize * cellSize
        for (y in food.indices) {
            for (x in food[y].indices) {
                if (Math.random() < FOOD_ADD_CHANCE) {
                    food[y][x] += foodPerTile
//                    if (food[y][x] > 1.0f) food[y][x] = 1.0f
                }
            }
        }
    }

    fun draw(graphics: PApplet) {
        graphics.noStroke()
        graphics.rectMode(CORNER)
        for (y in food.indices) {
            for (x in food[y].indices) {
                graphics.fill((1 - food[y][x]) * 255)
                graphics.rect((x * cellSize).toFloat(),
                    (y * cellSize).toFloat(),
                    cellSize.toFloat(),
                    cellSize.toFloat())
            }
        }
    }
}