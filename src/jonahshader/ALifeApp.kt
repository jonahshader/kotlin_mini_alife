package jonahshader

import processing.core.PApplet

class ALifeApp : PApplet() {
    private var noDraw = false
    private val world = World(64, 64, 600)
    private val noDrawDrawRate = 1000L //ms
    private var lastFrameDrawTime = 0L

    override fun settings() {
        size(512, 512)
        noSmooth()
    }

    override fun setup() {
        lastFrameDrawTime = System.currentTimeMillis()
        frameRate(144f)
    }

    override fun draw() {
        scale(8f)
        background(255)
        while (noDraw) {
            if (System.currentTimeMillis() > lastFrameDrawTime + noDrawDrawRate) {
                lastFrameDrawTime = System.currentTimeMillis()
                break
            } else {
                world.run()
            }
        }
        world.run()
        world.draw(this)
    }

    override fun keyPressed() {
        if (key.toLowerCase() == 'd') {
            noDraw = !noDraw;
        }
    }
}