package jonahshader

import jonahshader.datatypes.AddRemoveArrayList
import processing.core.PApplet

class World(val width: Int, val height: Int, creatureCount: Int) {
    val creatures = AddRemoveArrayList<Creature>()
    val food = Food(this, 2)

    init {
        for (i in 0 until creatureCount) {
            creatures.add(Creature((Math.random() * width).toFloat(), (Math.random() * height).toFloat(), this))
        }
    }

    fun run() {
        food.run()
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