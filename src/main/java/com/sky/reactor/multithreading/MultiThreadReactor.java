package com.sky.reactor.multithreading;

import java.io.IOException;

public class MultiThreadReactor {

    public static void main(String[] args) throws IOException {
        new Reactor(1234).start();
    }

}
