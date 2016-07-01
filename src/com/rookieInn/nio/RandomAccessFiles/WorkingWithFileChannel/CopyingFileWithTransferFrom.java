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
public class CopyingFileWithTransferFrom {

    public static void main(String[] args) throws IOException {
        Path copy_from = Paths.get("D:\\source\\movie", "唐人街探案.mp4");
        Path copy_to = Paths.get("E:/tmp", "唐人街探案.mp4");

        System.out.println("Using FileChannel.transferFrom method ...");

        FileChannel channel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
        FileChannel channel_to = FileChannel.open(copy_to, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));

        channel_to.transferFrom(channel_from, 0L, channel_from.size());

        System.out.println("end ...");
    }

}
