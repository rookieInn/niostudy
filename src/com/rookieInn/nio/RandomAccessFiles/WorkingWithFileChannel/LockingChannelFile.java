package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Locking a Channel's File
 *
 * File locking is a mechanism that restricts access to a file or other piece of data to ensure that two or more users can't modify the same simultaneously.
 * This prevent the classic interceding update scenario. Usually the file is locked when the first user accesses it and stays locked (can be read, but not modified)
 * until that user is finished with the file.
 *
 * The exact behavior of file locking is platform dependent. On some platforms, file locking is advisory(any application can access the file if the application does not check for a file lock),
 * while on others it is mandatory(file locking prevents any application from accessing a file).
 *
 * We can take advantage of file locking in Java applications through the NIO API.
 * However, there is no guarantee that the file locking mechanism will always work as you expect.
 * Underlying OS support or, sometimes, a faulty implementation may affect the expected behavior.
 * Keep in mind the following:
 *      a. File locks are held on behalf of the entire Java virtual machine.
 *         They are not suitable for controlling access to a file bu multiple threads within the same virtual machine.
 *      b. Windows takes care of locking directories and other structures for you, so a delete, rename, or write operation will fail if another process has the file open.
 *         Therefore, creating a Java lock over a system lock will fail.
 *      c. The Linux kernel manages a set of functions known as advisory locking mechanisms.
 *         In addition, you can enforce locking at the kernel level with mandatory locks.
 *         Therefore, when using Java locks, keep in mind this aspect.
 *
 * The FileChannel class provides four methods for file locking: two lock() methods and two tryLock() methods.
 * The lock() methods block the application and return null or throw an exception if the file is already locked.
 * There is one lock()/tryLock() method for retrieving an exclusive lock on this channel's file and one for retrieving a lock over a region of the channel's file-- this method also allows a lock to be shared.
 * Created by gxy on 2016/6/23.
 */
public class LockingChannelFile {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "vamos.txt");
        ByteBuffer buffer = ByteBuffer.wrap("Vamos Rafa!".getBytes());
        try {
            FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));

            /*
            Use the file channel to create a lock on the file.
            This method blocks until it can retrieve the lock.
            */
            FileLock lock = channel.lock();

            /*
            Try acquiring the lock without blocking.
            This method returns null or throws an exception if the file is already locked.
                try {
                    lock = channel.tryLock();
                } catch(OverlappingFileLockException e) {
                    File is already locked in this thread or virtual machine
                }
            */
            if (lock.isValid()) {
                System.out.println("Writing to a locked file ...");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.position(0);
                channel.write(buffer);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Release the lock
            lock.release();
            System.out.println("\nLock released!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}