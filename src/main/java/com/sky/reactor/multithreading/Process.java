package com.sky.reactor.multithreading;

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
            System.out.println("接收到来自(" + socketChannel.getRemoteAddress() + ")的消息 " + requestMsg + "，并交给线程(" + Thread.currentThread().getName() + ")处理");

            // 响应
            socketChannel.write(ByteBuffer.wrap(("你的消息我收到了" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
