package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Created by gxy on 2016/6/24.
 */
public class CopyingFileWithTransferTo {

    public static void main(String[] args) throws IOException {
        Path copy_from = Paths.get("E:/idea/nio", "美国队长.rmvb");
        Path copy_to = Paths.get("E:/tmp", "美国队长.rmvb");

        System.out.println("Using FileChannel.transferTo method ...");

        FileChannel channel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
        FileChannel channel_to = FileChannel.open(copy_to, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));

        channel_from.transferTo(0L, channel_from.size(), channel_to);

    }

}
