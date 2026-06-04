/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private final Supplier<T> factory;
    private final Queue<T> pool = new ConcurrentLinkedQueue<T>();

    public ObjectPool(Supplier<T> factory, int initialCapacity) {
        this.factory = factory;
        for (int i = 0; i < initialCapacity; ++i) {
            this.pool.offer(factory.get());
        }
    }

    public T acquire() {
        T item = this.pool.poll();
        return item != null ? item : this.factory.get();
    }

    public void release(T item) {
        this.pool.offer(item);
    }
}

