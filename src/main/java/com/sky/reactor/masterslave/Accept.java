package com.sky.reactor.masterslave;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

public class Accept implements Runnable {

    private final ServerSocketChannel serverSocketChannel;

    public Accept(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    private final AtomicLong atomicLong = new AtomicLong(0);

    private final int slaveCount = 1;
    private final SlaveReactor[] slaveReactors = new SlaveReactor[slaveCount];

    {
        for (int i = 0; i < slaveCount; i++) {
            slaveReactors[i] = new SlaveReactor("slave-" + i);
        }
    }

    @Override
    public void run() {
        try {
            // 接收客户端 socket
            SocketChannel sc = serverSocketChannel.accept();
            System.out.println("接收连接 " + sc.getRemoteAddress().toString());

            // 如果是一主多从，需要做负载均衡，将接收到的客户端 socket 均匀的注册到不同的 salve reactor
            int slaveIndex = getSlaveIndex();

            // 将客户端 socket 注册到 salve reactor
            slaveReactors[slaveIndex].register(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 该方法可以应用策略模式，更加符合开闭原则
    private int getSlaveIndex() {
        long index = atomicLong.getAndIncrement();
        long aa = index % slaveCount;
        return (int) aa;
    }

}
