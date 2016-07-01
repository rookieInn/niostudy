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
 * Write Characters at Different Positions
 *
 * First, add some text at the end of the preceding text,
 * Second, replace "Gonsales" with "Gonzalez"
 * Created by gxy on 2016/6/23.
 */
public class WriteCharacterAtDifferentPositions {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "MovistarOpen.txt");
        ByteBuffer buffer_1 = ByteBuffer.wrap("Great players participate in our tournament, like: Tommy Robredo, Fernando Gonzalez, Jose Acasuso or Thomaz Bellucci.".getBytes());
        ByteBuffer buffer_2 = ByteBuffer.wrap("Gozalez".getBytes());

        try {
            SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE));
            //append some text at the end
            channel.position(channel.size());
            while (buffer_1.hasRemaining()) {
                channel.write(buffer_1);
            }

            //replace "Gonsales" with "Gonzalez"
            channel.position(301);
            while (buffer_2.hasRemaining()) {
                channel.write(buffer_2);
            }
            buffer_1.clear();
            buffer_2.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
