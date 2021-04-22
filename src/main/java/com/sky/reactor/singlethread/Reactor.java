package com.sky.reactor.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor {

    private int port;

    public Reactor(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        // 创建 selector
        Selector selector = Selector.open();

        // 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将 ServerSocketChannel 关心的 accept 事件注册到 Selector
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 注册 accept 事件的处理器
        key.attach(new Accept());

        // 死循环接收链接
        while (true) {
            // 阻塞的
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                dispatcher(selectionKey);
                iterator.remove();
            }
        }
    }

    private void dispatcher(SelectionKey selectionKey) {
        Object object = selectionKey.attachment();
        if (object instanceof Accept) {
            Accept accept = (Accept) object;
            accept.accept(selectionKey.selector(), (ServerSocketChannel) selectionKey.channel());
        }
        if (object instanceof Worker) {
            Worker worker = (Worker) object;
            worker.work((SocketChannel) selectionKey.channel());
        }
    }

}
