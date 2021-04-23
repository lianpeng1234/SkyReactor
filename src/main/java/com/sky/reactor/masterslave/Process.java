package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Process implements Runnable {
    private final String requestMsg;

    private final SocketChannel socketChannel;

    private final String slaveSelectorName;

    public Process(String requestMsg, SocketChannel socketChannel, String slaveSelectorName) {
        this.requestMsg = requestMsg;
        this.socketChannel = socketChannel;
        this.slaveSelectorName = slaveSelectorName;
    }

    @Override
    public void run() {
        try {
            System.out.println(slaveSelectorName + " 接收到来自(" + socketChannel.getRemoteAddress() + ")的消息，并交给线程(" + Thread.currentThread().getName() + ")处理 " + requestMsg);

            // 响应
            socketChannel.write(ByteBuffer.wrap(("你的消息我收到了" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
