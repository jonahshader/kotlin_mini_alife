package jonahshader

import jonahshader.datatypes.AddRemoveArrayList
import processing.core.PApplet
import java.util.*

class World(val width: Int, val height: Int, creatureCount: Int) {
    val creatures = AddRemoveArrayList<Creature>()
    val food = Food(this, 2)
    val rand = Random()

    init {
        for (i in 0 until creatureCount) {
            creatures.add(Creature((Math.random() * width).toFloat(), (Math.random() * height).toFloat(), this))
        }
    }

    fun run() {
        food.run()

        creatures.parallelStream().forEach(Creature::run)

        for (creature in creatures) {
            creature.run()
        }
        creatures.update()
    }

    fun draw(graphics: PApplet) {
        food.draw(graphics)
        for (creature in creatures) {
            creature.draw(graphics)
        }
    }
}