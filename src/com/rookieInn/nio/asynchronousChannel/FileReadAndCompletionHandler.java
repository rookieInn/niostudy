package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by gxy on 2016/6/30.
 */
public class FileReadAndCompletionHandler {

    static Thread current;
    static final Path path = Paths.get("E:/idea/nio", "story.txt");

    public static void main(String[] args) {

        CompletionHandler<Integer, ByteBuffer> handler = new CompletionHandler<Integer, ByteBuffer>() {
            String encoding = System.getProperty("file.encoding");

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println(Thread.currentThread().getName() + " =>" + "Read bytes: " + result);
                attachment.flip();
                System.out.println(Charset.forName(encoding).decode(attachment).toString());
                attachment.clear();
                current.interrupt();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println(attachment);
                System.out.println("Error: " + exc);
                current.interrupt();
            }
        };

        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            current = Thread.currentThread();

            System.out.println("CurrentThread:" + current.getName());

            ByteBuffer buffer = ByteBuffer.allocate(100);
            channel.read(buffer, 0, buffer, handler);

            System.out.println("\nWaiting for reading operation to end ...\n");
            try {
                current.join();
            } catch (InterruptedException e) {
            }

            //now the buffer contains the read bytes
            System.out.println("\n\nClose everything and leave! Bye, bye ...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
