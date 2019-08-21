package jonahshader

import jonahshader.datatypes.AddRemoveArrayList
import processing.core.PApplet
import java.util.*

class World(val width: Int, val height: Int, creatureCount: Int) {
    val creatures = AddRemoveArrayList<Creature>()
    val food = Food(this, 2)
    val rand = Random()
    var time = 0L

    init {
        for (i in 0 until creatureCount) {
            creatures.add(Creature((Math.random() * width).toFloat(), (Math.random() * height).toFloat(), this))
        }
    }

    fun run() {
        food.run()
        creatures.parallelStream().forEach(Creature::run)
        creatures.update()
        time++

        if (time.rem(100) == 0L) {
            println("${time/100},${creatures.size}")
        }
    }

    fun draw(graphics: PApplet) {
        food.draw(graphics)
        for (creature in creatures) {
            creature.draw(graphics)
        }
    }
}