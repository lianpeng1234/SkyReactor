package com.sky.reactor.singlethread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Worker implements Runnable {

    private final SocketChannel socketChannel;

    public Worker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            // 读取消息
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            System.out.println("接收到来自(" + socketChannel.getRemoteAddress() + ")的消息 " + message);

            // 响应消息
            socketChannel.write(ByteBuffer.wrap(("你的消息我收到了" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
