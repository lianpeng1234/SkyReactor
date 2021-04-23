package com.sky.reactor.singlethread;

import java.io.IOException;

/**
 * 使用 telnet localhost 1234 进行测试
 */
public class SingleThreadReactor {

    public static void main(String[] args) throws IOException {
        new Reactor(1234).start();
    }

}
