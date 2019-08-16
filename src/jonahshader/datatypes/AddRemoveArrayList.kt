package jonahshader.datatypes

class AddRemoveArrayList<T> : ArrayList<T>() {
    private val addQueue = ArrayList<T>()
    private val removeQueue = ArrayList<T>()

    fun addQueued(t: T) : Boolean = addQueue.add(t)
    fun addAllQueued(elements: Collection<T>): Boolean = addQueue.addAll(elements)
    fun removeQueued(t: T) : Boolean = removeQueue.add(t)
    fun removeAllQueued(elements: Collection<T>): Boolean = removeQueue.addAll(elements)

    fun update() {
        addAll(addQueue)
        addQueue.clear()
        removeAll(removeQueue)
        removeQueue.clear()
    }
}