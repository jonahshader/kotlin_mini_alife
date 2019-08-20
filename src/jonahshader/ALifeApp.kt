package jonahshader

import processing.core.PApplet

class ALifeApp : PApplet() {
    private var noDraw = false
    private val world = World(64, 64, 100)

    override fun settings() {
        size(512, 512)
        noSmooth()
    }

    override fun setup() {

    }

    override fun draw() {
        scale(8f)
        background(255)
        if (noDraw)
            for (i in 1..100)
                world.run()
        else {
            world.run()
            world.draw(this)
        }
    }

    override fun keyPressed() {
        if (key.toLowerCase() == 'd') {
            noDraw = !noDraw;
        }
    }
}