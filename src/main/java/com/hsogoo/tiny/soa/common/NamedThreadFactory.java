package com.hsogoo.tiny.soa.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dell on 2017/4/26.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolId = new AtomicInteger();

    private final AtomicInteger nextId = new AtomicInteger();
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    private final ThreadGroup group;

    public NamedThreadFactory() {
        this("pool-" + poolId.incrementAndGet(), false, Thread.NORM_PRIORITY);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false, Thread.NORM_PRIORITY);
    }

    public NamedThreadFactory(String prefix, boolean daemon, int priority) {
        this.prefix = prefix + " #";
        this.daemon = daemon;
        this.priority = priority;
        SecurityManager s = System.getSecurityManager();
        group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {

        String name = prefix + nextId.getAndIncrement();
        Thread t = new Thread(group, r, name, 0);
        try {
            if (t.isDaemon()) {
                if (!daemon) {
                    t.setDaemon(false);
                }
            } else {
                if (daemon) {
                    t.setDaemon(true);
                }
            }

            if (t.getPriority() != priority) {
                t.setPriority(priority);
            }
        } catch (Exception ignored) {}

        return t;
    }

    public ThreadGroup getThreadGroup() {
        return group;
    }
}