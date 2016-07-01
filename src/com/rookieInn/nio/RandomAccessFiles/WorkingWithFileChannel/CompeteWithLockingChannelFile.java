package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Created by gxy on 2016/6/23.
 */
public class CompeteWithLockingChannelFile {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "vamos.txt");
        ByteBuffer buffer = ByteBuffer.wrap("Hai Hanescu !".getBytes());
        try {
            FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
            channel.position(0);
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
