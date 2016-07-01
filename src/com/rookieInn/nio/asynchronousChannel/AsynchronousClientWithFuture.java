package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by gxy on 2016/6/30.
 */
public class AsynchronousClientWithFuture {

    public static void main(String[] args) {
        final int port = 5555;
        final String ip = "127.0.0.1";
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        try {
            AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();
            if (asynchronousSocketChannel.isOpen()) {
                //set some options
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

                //connection
                Future<Void> future = asynchronousSocketChannel.connect(new InetSocketAddress(ip, port));
                Void connect = future.get();

                if (connect == null) {
                    System.out.println("Local address: " + asynchronousSocketChannel.getLocalAddress());

                    //transmitting data
                    asynchronousSocketChannel.write(helloBuffer).get();

                    while (asynchronousSocketChannel.read(buffer).get() != -1) {
                        buffer.flip();
                        charBuffer = decoder.decode(buffer);
                        System.out.println(charBuffer.toString());

                        if (buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }

                        int r = new Random().nextInt(100);
                        if(r == 50) {
                            System.out.println("50 was generated! Close the asynchronous socket channel!");
                            break;
                        } else {
                            randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                            asynchronousSocketChannel.write(randomBuffer).get();
                        }
                    }
                } else {
                    System.out.println("The connect cannot be established!");
                }
            } else {
                System.out.println("The asynchronous socket channel cannot be opened!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

}
