package com.hsogoo.tiny.soa.provider;

import com.hsogoo.tiny.soa.common.Invocation;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;

/**
 * Created by Dell on 2017/4/25.
 */
public class Provider {

    private Map remoteObjects = new HashMap();

    public void register(String className, Object remoteObject) {
        remoteObjects.put(className, remoteObject);
    }

    public void service() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("服务器启动.");
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);

            Invocation invocation = (Invocation) ois.readObject();
            System.out.println(invocation);
            invocation = invoke(invocation);
            oos.writeObject(invocation);

            ois.close();
            oos.close();
            socket.close();
        }
    }

    public Invocation invoke(Invocation invocation) {
        Object result = null;
        try {
            String className = invocation.getClassName();
            String methodName = invocation.getMethodName();
            Object[] params = invocation.getParams();
            Class classType = Class.forName(className);
            Class[] paramTypes = invocation.getParamTypes();
            Method method = classType.getMethod(methodName, paramTypes);
            Object remoteObject = remoteObjects.get(className);
            if (remoteObject == null) {
                throw new Exception(className + "的远程对象不存在");
            } else {
                result = method.invoke(remoteObject, params);
            }
        } catch (Exception e) {
            result = e;
        }

        invocation.setResult(result);
        return invocation;
    }

    public static void main(String[] args) throws Exception {
        Provider provider = new Provider();
        provider.register("com.hsogoo.tiny.soa.spi.HelloService", new HelloServiceImpl());
        provider.service();
    }
}
