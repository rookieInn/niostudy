package com.rookieInn.nio.RandomAccessFiles.SeekableByteChannelPosition;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Copy a Portion of a File from the Beginning to the End
 * Created by gxy on 2016/6/23.
 */
public class CopyPositionOfFileFromBeginingToEnd {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "HeinekenOpen.txt");
        ByteBuffer copy = ByteBuffer.allocate(25);
        copy.put("\n".getBytes());
        try {
            SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
            int nbytes;
            do {
                nbytes = channel.read(copy);
            } while (nbytes != -1 && copy.hasRemaining());

            copy.flip();

            channel.position(channel.size());
            while (copy.hasRemaining()) {
                channel.write(copy);
            }
            copy.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
