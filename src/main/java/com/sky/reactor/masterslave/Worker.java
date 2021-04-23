package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Worker implements Runnable {

    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30));

    private final SocketChannel socketChannel;

    private final String slaveSelectorName;

    public Worker(SocketChannel socketChannel, String slaveSelectorName) {
        this.socketChannel = socketChannel;
        this.slaveSelectorName = slaveSelectorName;
    }

    @Override
    public void run() {
        try {
            // 注意读取 socket 中的数据只能在 worker 中，不可以放到线程池中，否则会死循环（原因本人未查明）
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);

            threadPoolExecutor.submit(new Process(message, socketChannel, slaveSelectorName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
