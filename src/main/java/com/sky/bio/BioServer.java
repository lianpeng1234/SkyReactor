package com.sky.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 使用 telnet localhost 6789 模拟客户端
public class BioServer {

    public static void main(String[] args) throws IOException {
        int port = 6789;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            // 等待连接是阻塞的
            Socket clientSocket = serverSocket.accept();
            System.out.println("accept客户端连接 " + clientSocket.getRemoteSocketAddress().toString());

            // 这里每新建一个连接就创建一个线程，来模拟多线程的情况（可以使用线程池）
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            // 读取数据是阻塞的
                            InputStream inputStream = clientSocket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            String str = reader.readLine();
                            System.out.println("接收到的消息是 " + str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        }
    }


}
