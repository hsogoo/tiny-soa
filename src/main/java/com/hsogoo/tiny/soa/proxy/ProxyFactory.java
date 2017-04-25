package com.hsogoo.tiny.soa.proxy;

import com.hsogoo.tiny.soa.common.Invocation;
import com.hsogoo.tiny.soa.common.RemoteException;
import com.hsogoo.tiny.soa.transport.Connector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Dell on 2017/4/25.
 */
public class ProxyFactory {
    public static Object getProxy(final Class classType, final String host, final int port) {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object args[])
                    throws Exception {
                Connector connector = null;
                try {
                    connector = new Connector(host, port);
                    Invocation invocation = new Invocation(classType.getName(),
                            method.getName(), method.getParameterTypes(), args);
                    connector.send(invocation);
                    invocation = (Invocation) connector.receive();
                    Object result = invocation.getResult();
                    if (result instanceof Throwable)
                        throw new RemoteException((Throwable) result);
                    else
                        return result;
                } finally {
                    if (connector != null) connector.close();
                }
            }
        };

        return Proxy.newProxyInstance(classType.getClassLoader(),
                new Class[]{classType},
                handler);
    }
}