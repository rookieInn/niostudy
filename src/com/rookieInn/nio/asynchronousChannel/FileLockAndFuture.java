package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * File Lock
 *
 * Sometimes you need to acquire an exclusive lock on a channel's file before performing another I/O operation,
 * such as reading or writing. AsynchronousFileChannel provides a lock() method for the Future from and a lock() method for CompletionHandler
 *      public final Future<FileLock> lock()
 *      public final <A> void lock(A attachment, CompletionHandler<FileLock, ? super A> handler)
 * The following applications user the lock() method with the Future form for locking a file.
 * We will wait to acquire the lock by calling the Future.get() method, and, afterward, we will write some bytes into our file.
 * Again, we call the get() method that will wait until the new bytes are written and, finally release the lock.
 *
 * Created by gxy on 2016/6/30.
 */
public class FileLockAndFuture {

    public static void main(String[] args) {
        String str = "Argentines At Home In Buenos Aires Cathedral\n " +
                "The Copa Claro is the third stop of the four-tournament Latin American swing, " +
                "and is contested on clay at the Buenos Aires Lawn Tennis Club, known as the Cathedral of Argentinean tennis. " +
                "An Argentine has reached the final in nine of the 11 editions of the ATP World Tour 250 tournament, " +
                "with champions including Guillermo Coria, Gaston Gaudio, Juan Monaco and David  Nalbandian.";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        Path path = Paths.get("E:/idea/nio", "CopaClaro.txt");
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)){
            Future<FileLock> featureLock = channel.lock();
            System.out.println("Waiting for the file to be locked ...");
            FileLock lock = featureLock.get();

            if (lock.isValid()) {
                Future<Integer> featureWrite = channel.write(buffer, 0);
                System.out.println("Waiting for the bytes to be written ...");
                int written = featureWrite.get();

                System.out.println("I've written " + written + " bytes into " + path.getFileName() + " locked file!");
                lock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
