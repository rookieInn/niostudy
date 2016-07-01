package com.rookieInn.nio.asynchronousChannel;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by gxy on 2016/6/30.
 */
public class FileReadAndFutureTimeout {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int bytesRead = 0;
        Future<Integer> result = null;

        Path path = Paths.get("E:/idea/nio", "story.txt");

        try(AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            result = asynchronousFileChannel.read(buffer, 0);
            result.get(1, TimeUnit.NANOSECONDS);

            if (result.isDone()) {
                System.out.println("The result is available!");
                System.out.println("Read bytes: " + bytesRead);
            }
        } catch (Exception e) {
            if (e instanceof TimeoutException) {
                if (result != null) {
                    result.cancel(true);
                }
                System.out.println("The result is not available!");
                System.out.println("The read task was cancelled ? " + result.isCancelled());
                System.out.println("Read bytes: " + bytesRead);
            } else {
                System.err.println(e);
            }
        }
    }

}
