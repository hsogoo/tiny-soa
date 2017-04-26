package com.hsogoo.tiny.soa.registry;

import com.hsogoo.tiny.soa.common.NamedThreadFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Dell on 2017/4/26.
 */
public abstract class AbstractRegistryService implements RegistryService {

    private final LinkedBlockingQueue<RegisterMeta> queue = new LinkedBlockingQueue<RegisterMeta>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("registry.executor"));
    // Consumer已订阅的信息
    private final Set<RegisterMeta.ServiceMeta> subscribeSet = new HashSet<RegisterMeta.ServiceMeta>();
    // Provider已发布的注册信息
    private final Set<RegisterMeta.ServiceMeta> registerMetaSet = new HashSet<RegisterMeta.ServiceMeta>();

    private final ConcurrentMap<RegisterMeta.ServiceMeta, CopyOnWriteArrayList<NotifyListener>> subscribeListeners =
            new ConcurrentHashMap<RegisterMeta.ServiceMeta, CopyOnWriteArrayList<NotifyListener>>();
    @Override
    public void register(RegisterMeta meta) {
        if(queue.add(meta)){
            doRegister(meta);
        }
    }

    @Override
    public void unregister(RegisterMeta meta) {
        if (queue.remove(meta)) {
            doUnregister(meta);
        }
    }

    @Override
    public void subscribe(RegisterMeta.ServiceMeta serviceMeta, NotifyListener listener) {
        CopyOnWriteArrayList<NotifyListener> listeners = subscribeListeners.get(serviceMeta);
        if(listeners == null){
            CopyOnWriteArrayList<NotifyListener> newListeners = new CopyOnWriteArrayList<NotifyListener>();
            newListeners.add(listener);
            listeners = subscribeListeners.putIfAbsent(serviceMeta, newListeners);
            if (listeners == null) {
                listeners = newListeners;
            }
        }
        listeners.add(listener);
        doSubscribe(serviceMeta);
    }

    protected abstract void doSubscribe(RegisterMeta.ServiceMeta serviceMeta);

    protected abstract void doRegister(RegisterMeta meta);

    protected abstract void doUnregister(RegisterMeta meta);
}
