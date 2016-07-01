package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * Created by gxy on 2016/6/30.
 */
public class AsynchronousServerWithCompletionHandler {

    public static void main(String[] args) {
        final int port = 5555;
        final String ip = "127.0.0.1";

        //create an asynchronous server socket channel bound to the default group
        try {
            AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            if (asynchronousServerSocketChannel.isOpen()) {
                //set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                //bind the server socket channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(ip, port));

                //display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");

                asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                   final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                    @Override
                    public void completed(AsynchronousSocketChannel result, Void attachment) {
                        asynchronousServerSocketChannel.accept(null, this);

                        try {
                            System.out.println("Incoming connection from: " + result.getRemoteAddress());

                            //transmitting data
                            while (result.read(buffer).get() != -1) {
                                buffer.flip();
                                result.write(buffer).get();

                                if (buffer.hasRemaining()) {
                                    buffer.compact();
                                } else {
                                    buffer.clear();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                result.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        asynchronousServerSocketChannel.accept(null, this);

                        throw new UnsupportedOperationException("Cannot accept connections!");
                    }
                });

                //wait
                System.in.read();

            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
