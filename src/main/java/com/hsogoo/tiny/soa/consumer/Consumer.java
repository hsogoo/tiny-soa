package com.hsogoo.tiny.soa.consumer;

import com.hsogoo.tiny.soa.proxy.ProxyFactory;
import com.hsogoo.tiny.soa.spi.HelloService;

/**
 * Created by Dell on 2017/4/25.
 */
public class Consumer {

    public static void main(String[] args) {
        HelloService helloService = (HelloService) ProxyFactory.getProxy(HelloService.class, "localhost", 8000);
        System.out.println(helloService.sayHello("tiny-soa"));
    }

}
