package com.rookieInn.nio.RandomAccessFiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

/**
 * The following code snippet(written for UNIX and other POSIX file system) create a file with a specific set of file permissions.
 * This code create the file email.txt in the home \rafaelnadal\email directory or appends to it if it already exist.
 * The email.txt file is created with read and write permissions for the owner and read-only permissions for the group.
 *
 * Created by gxy on 2016/6/23.
 */
public class SeekableByteChannelAndFileAttribute {

    public static void main(String[] args) {
        Path path = Paths.get("home/rafaelnadal/email", "email.txt");
        ByteBuffer buffer = ByteBuffer.wrap("Hi Rafa, I want to congratulate you for the amazing match that you played ... ".getBytes());
        //create the custom permissions attribute for the email.txt file
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        //write a file using SeekableByteChannel
        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.APPEND), attr)) {
            int write = seekableByteChannel.write(buffer);
            System.out.println("Number of written bytes: " + write);
        } catch (IOException e) {
            System.err.println(e);
        }
        buffer.clear();
    }

}
