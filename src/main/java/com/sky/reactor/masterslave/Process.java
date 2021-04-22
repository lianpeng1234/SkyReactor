package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Process implements Runnable {
    private final String requestMsg;

    private final SocketChannel socketChannel;

    public Process(String requestMsg, SocketChannel socketChannel) {
        this.requestMsg = requestMsg;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " 接收到的消息是 " + requestMsg);

            // 响应
            socketChannel.write(ByteBuffer.wrap(("你的消息我收到了" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
