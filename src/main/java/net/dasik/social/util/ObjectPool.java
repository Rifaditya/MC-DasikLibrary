package net.dasik.social.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * A thread-safe, lock-free object pool.
 * Used to recycle short-lived objects like Signals.
 * 
 * @param <T> The type of object to pool.
 */
public class ObjectPool<T> {
    private final Supplier<T> factory;
    private final Queue<T> pool = new ConcurrentLinkedQueue<>();

    public ObjectPool(Supplier<T> factory, int initialCapacity) {
        this.factory = factory;
        for (int i = 0; i < initialCapacity; i++) {
            pool.offer(factory.get());
        }
    }

    public T acquire() {
        T item = pool.poll();
        return item != null ? item : factory.get();
    }

    public void release(T item) {
        pool.offer(item);
    }
}
