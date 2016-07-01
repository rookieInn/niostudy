package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Copying Files with FileChannel and a Direct or Non-direct ByteBuffer
 *
 * Created by gxy on 2016/6/23.
 */
public class CopyingFileWithDirectOrNonDirectByteBuffer {

    public static void main(String[] args) {
        final Path copy_from = Paths.get("D:\\source\\movie", "叶问3.mp4");
        final Path copy_to = Paths.get("E:/tmp/叶问3.mp4");
        int bufferSizeMB = 10;
        int bufferSize = bufferSizeMB * 1024 * 1024;

        System.out.println("Using FileChannel and direct buffer ...");
        try {
            FileChannel channel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel channel_to = FileChannel.open(copy_to, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));

            // Allocate a direct ByteBuffer
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
            //To use a non-direct ByteBuffer
            //ByteBuffer bytebuffer = ByteBuffer.allocate(bufferSize);

            //Read data from file into ByteBuffer
            int bytesCount;
            int count = 1;
            while ((bytesCount = channel_from.read(buffer)) > 0) {
                // flip the buffer which set the limit to current position, and position to 0
                buffer.flip();
                //write data form ByteBuffer to file
                channel_to.write(buffer);
                System.out.println("copying "+ (count++) +" --------------------> " + bytesCount + "B ");

                //for the next read
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
