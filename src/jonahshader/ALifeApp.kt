package jonahshader

import processing.core.PApplet

class ALifeApp : PApplet() {

    private val world = World(256, 256, 8)

    override fun settings() {
        size(640, 480);
    }

    override fun setup() {

    }

    override fun draw() {
        background(255)
        world.run()
        world.draw(this)
    }
}