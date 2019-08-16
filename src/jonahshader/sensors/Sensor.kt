package jonahshader.sensors

import jonahshader.datatypes.ComplexNumber
import processing.core.PApplet

interface Sensor {
    fun getValue() : Float
    fun run()
    fun draw(graphics: PApplet)
}