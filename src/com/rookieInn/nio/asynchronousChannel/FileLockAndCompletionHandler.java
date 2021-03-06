package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by gxy on 2016/6/30.
 */
public class FileLockAndCompletionHandler {

    static Thread current;

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "CopaClaro.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            current = Thread.currentThread();

            asynchronousFileChannel.lock("Lock operation status:", new
                    CompletionHandler<FileLock, Object>() {
                        @Override
                        public void completed(FileLock result, Object attachment) {
                            System.out.println(attachment + " " + result.isValid());
                            if (result.isValid()) {
                                //... processing ...
                                System.out.println("Processing the locked file ...");
                                //...
                                try {
                                    result.release();
                                } catch (IOException ex) {
                                    System.err.println(ex);
                                }
                            }
                            current.interrupt();
                        }
                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println(attachment);
                            System.out.println("Error:" + exc);
                            current.interrupt();
                        }
                    });
            System.out.println("Waiting for file to be locked and process ... \n");
            try {
                current.join();
            } catch (InterruptedException e) {
            }
            System.out.println("\n\nClosing everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

}
