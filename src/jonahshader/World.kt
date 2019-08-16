package jonahshader

import jonahshader.datatypes.AddRemoveArrayList
import processing.core.PApplet

class World(val width: Int, val height: Int, creatureCount: Int) {
    private val creatures = AddRemoveArrayList<Creature>()


    init {
        for (i in 0 until creatureCount) {
            creatures.add(Creature((Math.random() * width).toFloat(), (Math.random() * height).toFloat(), this))
        }
    }

    fun run() {
        for (creature in creatures) {
            creature.run()
        }
        creatures.update()
    }

    fun draw(graphics: PApplet) {
        for (creature in creatures) {
            creature.draw(graphics)
        }
    }
}