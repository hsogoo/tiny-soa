package com.hsogoo.tiny.soa.registry;

/**
 * Created by Dell on 2017/4/26.
 */
public interface NotifyListener {

    void notify(RegisterMeta registerMeta, NotifyEvent event);

    enum NotifyEvent {
        CHILD_ADDED,
        CHILD_REMOVED
    }
}
