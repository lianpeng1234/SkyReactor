package com.sky.reactor.masterslave;

import java.io.IOException;

public class MasterSlaveReactor {

    public static void main(String[] args) throws IOException {
        new MasterReactor(1234).start();
    }

}
