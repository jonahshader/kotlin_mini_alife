package jonahshader

class Food(world: World, val cellSize: Int) {
    private val FOOD_ACCUMULATE_RATE = 10f
    private val FOOD_ADD_CHANCE = 0.05f
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

    fun readCell(x: Float, y: Float) : Float = food[(y / cellSize).toInt()][(x / cellSize).toInt()]

    fun run() {
        // add some food to the system then diffuse
        val foodPerTile = FOOD_ACCUMULATE_RATE * cellSize * cellSize;
    }
}