package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Accept {


    private SlaveReactor[] slaveReactors = new SlaveReactor[1];

    {
        slaveReactors[0] = new SlaveReactor();

//        slaveReactors[1] = new SlaveReactor();
//        slaveReactors[1].start();

    }

    public void accept(Selector selector, ServerSocketChannel serverSocketChannel) {
        try {
            // 接收客户端 socket
            SocketChannel sc = serverSocketChannel.accept();
            System.out.println("接收连接 " + sc.getRemoteAddress().toString());

            // 如果是一主多从，需要做负载均衡，将接收到的客户端 socket 均匀的注册到不同的 salve reactor
            // load balance

            // 将客户端 socket 注册到 salve reactor
            slaveReactors[0].register(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
