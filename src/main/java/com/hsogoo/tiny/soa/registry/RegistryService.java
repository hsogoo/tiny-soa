package com.hsogoo.tiny.soa.registry;

/**
 * Created by Dell on 2017/4/26.
 */
public interface RegistryService {

    /**
     * Register service to registry server.
     */
    void register(RegisterMeta meta);

    /**
     * Unregister service to registry server.
     */
    void unregister(RegisterMeta meta);

    /**
     * Subscribe a service from registry server.
     */
    void subscribe(RegisterMeta.ServiceMeta serviceMeta, NotifyListener listener);
}
