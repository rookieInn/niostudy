package com.rookieInn.nio.socketApi;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by gxy on 2016/6/24.
 */
public class BlockingTcpServer {

    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        final String IP = "127.0.0.1";

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        //create a new server socket channel
        try(ServerSocketChannel channel = ServerSocketChannel.open()) {

            //continue if it was successfully created
            if (channel.isOpen()) {

                //set the blocking mode
                channel.configureBlocking(true);
                //set some options
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                //bind the server socket channel to local address
                channel.bind(new InetSocketAddress(IP, DEFAULT_PORT));

                //display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");

                //wait for incoming connections
                while (true) {
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("Incoming connection from: " + socketChannel.getRemoteAddress());

                    //transmitting data
                    while (socketChannel.read(buffer) != -1) {
                        buffer.flip();

                        socketChannel.write(buffer);
                        if (buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }
                    }
                }
            } else {
                System.out.println("The service socket channel cannot be opened!");
            }
        } catch (Exception e) {

        }
    }



}
