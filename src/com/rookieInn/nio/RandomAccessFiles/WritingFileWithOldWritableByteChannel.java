package com.rookieInn.nio.RandomAccessFiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Writing a File with the Old WritableByteChannel Interface
 *
 * Even if we use a WritableByteChannel, we still need to explicitly the WRITE option.
 * The APPEND option is optional, and is specific to the preceding example.
 * Created by gxy on 2016/6/23.
 */
public class WritingFileWithOldWritableByteChannel {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "story.txt");

        try {
            //write a file using WritableByteChannel
           WritableByteChannel channel =  Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.APPEND));
            ByteBuffer buffer = ByteBuffer.wrap("Vamos Rafa!".getBytes());
            int write = channel.write(buffer);
            System.out.println("Number of written bytes: " + write);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
