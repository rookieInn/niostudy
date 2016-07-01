package com.rookieInn.nio.RandomAccessFiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Writing a File With SeekableByteChannel
 *
 *  When you write a file, there a few common cases that involve combining the open options:
 *      a. To write into a file that exists, at the Beginning, use Write
 *      b. To write into a file that exists, at the end, use WRITE and APPEND
 *      c. To write into a file that exits and clean up its content before writing, use WRITE and TRUNCATE_EXISTING
 *      d. To write into a file that does not exist, use CREATE (or CREATE_NEW) and WRITE
 * Created by gxy on 2016/6/23.
 */
public class WritingFileWithSeekableByteChannel {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "story.txt");
        //write a file using SeekableByteChannel
        try {
            SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));

            ByteBuffer buffer = ByteBuffer.wrap("Rafa Nadal produced another masterclass of clay-court tennis to win his fifth French Open title ...".getBytes());

            int write = channel.write(buffer);
            System.out.println("Number of written bytes: " + write);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
