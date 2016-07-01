package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * Asynchronous File Channel Example
 * File Read and Future
 *
 * Created by gxy on 2016/6/30.
 */
public class FileReadAndFuture {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        String encoding = System.getProperty("file.encoding");

        Path path = Paths.get("E:/idea/nio", "story.txt");
        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            Future<Integer> result = channel.read(buffer, 0);
            while (!result.isDone()) {
                System.out.println("Do something else while reading ...");
            }
            System.out.println("Read done: " + result.isDone());
            System.out.println("Bytes read: " + result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.flip();
        System.out.println(Charset.forName(encoding).decode(buffer).toString());
        buffer.clear();
    }

}
