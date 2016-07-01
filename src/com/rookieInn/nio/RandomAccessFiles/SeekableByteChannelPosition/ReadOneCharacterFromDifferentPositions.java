package com.rookieInn.nio.RandomAccessFiles.SeekableByteChannelPosition;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Read One Character from Different Positions
 *
 * We start with a simple example that reads exactly one character from a text file from the first, middle and last positions.
 * Created by gxy on 2016/6/23.
 */
public class ReadOneCharacterFromDifferentPositions {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "MovistarOpen.txt");
        ByteBuffer buffer = ByteBuffer.allocate(1);
        String encoding = System.getProperty("file.encoding");

        try {
            SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ);
            System.out.println("channel size: " + channel.size());
            // the initial position should be 0 anyway
            channel.position(0);
            System.out.println("Reading one character from position: " + channel.position());
            channel.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName(encoding).decode(buffer));
            buffer.rewind();

            //get into the middle
            channel.position(channel.size()/2);
            System.out.println("\nReading one character from position: " + channel.position());
            channel.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName(encoding).decode(buffer));
            buffer.rewind();

            // get to the end
            channel.position(channel.size() - 1);
            System.out.println("\nReading one character from position: " + channel.position());
            channel.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName(encoding).decode(buffer));
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
