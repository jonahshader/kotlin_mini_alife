package jonahshader.datatypes;

import java.util.ArrayList;
import java.util.Collection;

public class AddRemoveArrayList<T> extends ArrayList<T> {
    private ArrayList<T> addQueue, removeQueue;

    public AddRemoveArrayList() {
        addQueue = new ArrayList<>();
        removeQueue = new ArrayList<>();
    }

    public boolean addQueued(T t) {
        return addQueue.add(t);
    }

    public boolean addAllQueued(Collection<? extends T> c) {
        return addQueue.addAll(c);
    }

    public boolean removeQueue(T t) {
        return removeQueue.add(t);
    }

    public boolean removeAllQueue(Collection<? extends T> c) {
        return removeQueue.addAll(c);
    }

    public void update() {
        addAll(addQueue);
        addQueue.clear();
        removeAll(removeQueue);
        removeQueue.clear();
    }
}
