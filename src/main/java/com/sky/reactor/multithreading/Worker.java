package com.sky.reactor.multithreading;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class Worker implements Runnable {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30));

    private final SocketChannel socketChannel;

    public Worker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            // 注意读取 socket 中的数据只能在 worker 中，不可以放到线程池中，否则会死循环（原因本人未查明）
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);

            // 将消息交给 work thread 处理
            threadPoolExecutor.submit(new Process(message, socketChannel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
