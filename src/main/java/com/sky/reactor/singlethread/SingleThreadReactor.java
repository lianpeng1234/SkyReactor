package com.sky.reactor.singlethread;

import java.io.IOException;

public class SingleThreadReactor {

    public static void main(String[] args) throws IOException {
        new Reactor(1234).start();
    }

}
