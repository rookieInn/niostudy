package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * Created by gxy on 2016/6/30.
 */
public class AsynchronousServerWithFuture {

    public static void main(String[] args) {
        final int port = 5555;
        final String ip = "127.0.0.1";
        final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        try(AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                asynchronousServerSocketChannel.bind(new InetSocketAddress(ip, port));

                System.out.println("Waiting for connections ...");

                while(true) {
                    Future<AsynchronousSocketChannel> future = asynchronousServerSocketChannel.accept();

                    AsynchronousSocketChannel asynchronousSocketChannel = future.get();

                    System.out.println("Incoming connection from: " + asynchronousSocketChannel.getRemoteAddress());

                    while (asynchronousSocketChannel.read(buffer).get() != -1) {
                        buffer.flip();
                        asynchronousSocketChannel.write(buffer).get();
                        if (buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }
                    }
                    System.out.println(asynchronousSocketChannel.getRemoteAddress() + " was successfully served!");
                }
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (Exception e) {
            System.err.println(e);
        }


    }

}