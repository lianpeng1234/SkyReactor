package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SlaveReactor {

    private Selector selector;

    private volatile boolean status = false;

    {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(SocketChannel sc) {
        try {
            // 设置为非阻塞
            sc.configureBlocking(false);
            // 将 socket 关心的 read 事件注册到 selector
            SelectionKey key = sc.register(selector, SelectionKey.OP_READ);
            // 注册 read 事件的处理器
            key.attach(new Worker());

            // 开始监听事件
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        if (status) {
            return;
        }
        status = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // 死循环接收链接
                while (true) {
                    // 阻塞的
                    try {
                        selector.select();
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            dispatcher(selectionKey);
                            iterator.remove();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private void dispatcher(SelectionKey selectionKey) {
        Object object = selectionKey.attachment();
        if (object instanceof Worker) {
            Worker worker = (Worker) object;
            worker.work((SocketChannel) selectionKey.channel());
        }
    }

}
