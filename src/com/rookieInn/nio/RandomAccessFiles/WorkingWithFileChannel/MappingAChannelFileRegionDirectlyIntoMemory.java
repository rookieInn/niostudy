package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Mapping a Channel's File Region Directly into Memory
 *
 * One of the great FileChannel facilities is the capability to map a region of a channel's file directly into memory.
 * This is possible thanks to the FileChannel.map() method, which gets the following three arguments:
 *      a. mode: Mapping a region into memory can be accomplished in one of the three modes:
 *               MapMode.READ_ONLY(read-only mapping; writing attempts will throw ReadOnlyBufferException),
 *               MapMode.READ_WRITE(read/write mapping; changes in the resulting buffer can be propagated to the file and can be visible from other programs that map the same file),
 *               MapMode.PRIVATE(copy-on-write mapping; changes in the resulting buffer can't be propagated to the file and aren't visible from other programs).
 *      b. position: The mapped region starts at the indicated position within the file(non-negative).
 *      c. size: Indicates the size of the mapped region (0 ≤ size ≤ Integer.MAX_VALUE).
 *
 *  The map() method will return a MappedByteBuffer that actually represents the extracted region.
 *  This extends the ByteBuffer with the following three methods:
 *      a. force(): Forces the changed over buffer to be propagated to the originating file
 *      b. load(): Loads the buffer content into physical memory
 *      c. isLoaded(): Verifies whether the buffer content is in physical memory
 * Created by gxy on 2016/6/23.
 */
public class MappingAChannelFileRegionDirectlyIntoMemory {

    public static void main(String[] args) {
        Path path = Paths.get("E:/idea/nio", "BrasilOpen.txt");
        MappedByteBuffer buf = null;
        try {
            FileChannel channel = FileChannel.open(path, EnumSet.of(StandardOpenOption.READ));
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (buf != null) {
            try {
                Charset charset = Charset.defaultCharset();
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer =decoder.decode(buf);
                String content = charBuffer.toString();
                System.out.println(content);
                buf.clear();
            } catch (CharacterCodingException e) {
                e.printStackTrace();
            }
        }
    }

}
