package com.rookieInn.nio.RandomAccessFiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by gxy on 2016/6/23.
 */
public class ReadingFileWithSeekableByteChannel {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio/change.html");
        try {
            //read a file using SeekableByteChannel
            SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println(System.getProperty("file.encoding"));
            String encoding = "UTF-8";
            buffer.clear();

            while (channel.read(buffer) > 0) {
                buffer.flip();
                System.out.println(Charset.forName(encoding).decode(buffer));
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
