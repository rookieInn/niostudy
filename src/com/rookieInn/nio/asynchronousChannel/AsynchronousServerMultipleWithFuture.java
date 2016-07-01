package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.*;

/**
 * accept multiple clients
 *
 *
 * Created by gxy on 2016/6/30.
 */
public class AsynchronousServerMultipleWithFuture {

    public static void main(String[] args) {
        final int port = 5555;
        final String ip = "127.0.0.1";
        ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        //create asynchronous server socket channel bound to the default group
        try(AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                //set some options
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                //bind the server channel to local address
                asynchronousServerSocketChannel.bind(new InetSocketAddress(ip, port));

                //display a waiting message while ... waiting clients
                System.out.println("Waiting for connections ...");

                while (true) {
                    Future<AsynchronousSocketChannel> future = asynchronousServerSocketChannel.accept();
                    try{
                        AsynchronousSocketChannel asynchronousSocketChannel = future.get();
                        Callable<String> worker = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String host = asynchronousSocketChannel.getRemoteAddress().toString();
                                System.out.println("Incoming connection from: " + host);
                                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                                //transmitting data
                                while (asynchronousSocketChannel.read(buffer).get() != -1){
                                    buffer.flip();

                                    asynchronousSocketChannel.write(buffer).get();

                                    if (buffer.hasRemaining()) {
                                        buffer.compact();
                                    } else {
                                        buffer.clear();
                                    }
                                }

                                asynchronousServerSocketChannel.close();
                                System.out.println(host + " was successfully served!");
                                return host;
                            }
                        };
                        executor.submit(worker);
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println(e);
                        System.err.println("\n Server is shutting down ...");
                        //this will make the executor accept no new threads
                        // and finish all existing threads in the queue
                        executor.shutdown();
                        //wait until all threads are finished
                        while (!executor.isTerminated()) {
                        }
                        break;
                    }
                }
            } else {
                System.out.println("The asynchronous server-socket channel cannot be opened!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
