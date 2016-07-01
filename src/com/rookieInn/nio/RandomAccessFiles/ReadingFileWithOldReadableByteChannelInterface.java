package com.rookieInn.nio.RandomAccessFiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Reading a File with the Old ReadableByteChannel Interface
 *
 * SeekableByteChannel extends ReadableByteChannel, WritableChannel Interface
 * so we can use ReadableByteChannel channel = File.newByteChannel();
 * In this way, there is no need to specify the READ option.
 *
 * Created by gxy on 2016/6/23.
 */
public class ReadingFileWithOldReadableByteChannelInterface {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "change.html");
        try {
            ReadableByteChannel channel = Files.newByteChannel(path);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(channel.read(buffer) > 0) {
                buffer.flip();
                System.out.println(Charset.forName(System.getProperty("file.encoding")).decode(buffer));
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
