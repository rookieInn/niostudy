package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Copying Files with FileChannel.map()
 *
 * Created by gxy on 2016/6/24.
 */
public class CopyingFileWithFileMap {

    public static void main(String[] args) throws IOException {
        Path copy_from = Paths.get("D:\\source\\movie", "疯狂动物城.mkv");
        Path copy_to = Paths.get("E:/tmp", "疯狂动物城.mkv");

        System.out.println("Using FileChannel.map method ...");
        FileChannel channel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
        FileChannel channel_to = FileChannel.open(copy_to, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));

        MappedByteBuffer buffer = channel_from.map(FileChannel.MapMode.READ_ONLY, 0, channel_from.size());
        channel_to.write(buffer);
        buffer.clear();
        System.out.println("end ...");
    }

}
