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
 * Replace a File Portion with Truncate Capability
 *
 * Created by gxy on 2016/6/23.
 */
public class ReplaceFilePortionWithTruncateCapability {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("E:/idea/nio", "BrasilOpen.txt");
        ByteBuffer buffer = ByteBuffer.wrap("The tournament has taken a lead in environmental conservation efforts, with highlights including the planting of 500 trees to neutralise carbon emissions and providing recyclable materials to local children for use in craft work.".getBytes());

        SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
        channel.truncate(200);
        System.out.println(channel.size());
        channel.position(channel.size() - 1);
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        buffer.clear();
    }

}
