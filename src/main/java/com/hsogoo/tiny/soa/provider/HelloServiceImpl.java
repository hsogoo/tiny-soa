package com.hsogoo.tiny.soa.provider;

import com.hsogoo.tiny.soa.spi.HelloService;

/**
 * Created by Dell on 2017/4/25.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
