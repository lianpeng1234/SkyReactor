package com.sky.reactor.multithreading;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Accept {

    public void accept(Selector selector, ServerSocketChannel serverSocketChannel) {
        try {
            // 接收客户端 socket
            SocketChannel sc = serverSocketChannel.accept();
            System.out.println("接收连接 " + sc.getRemoteAddress().toString());

            // 设置为非阻塞
            sc.configureBlocking(false);

            // 将 socket 关心的 read 事件注册到 selector
            SelectionKey key = sc.register(selector, SelectionKey.OP_READ);
            // 注册 read 事件的处理器
            key.attach(new Worker());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
