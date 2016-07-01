package com.rookieInn.nio.RandomAccessFiles.WorkingWithFileChannel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.EnumSet;

/**
 * Benchmarking FileChannel Copy Capabilities
 *
 * Some solution of copy file
 *      a. FileChannel and a non-direct ByteBuffer
 *      b. FileChannel and a direct ByteBuffer
 *      c. FileChannel.transferTo()
 *      d. FileChannel.transferFrom()
 *      e. FileChannel.map()
 *      f. Using buffered stream and a byte array
 *      g. Using unbuffered streams and a byte array
 *      h. Files.copy() (Path to Path, InputStream to Path, and Path to OutputStream)
 * The test was made under to following conditions:
 *      1. copied file type: MP4 video
 *      2. copied file size:  MB
 *      3. Buffer size tested: 4KB, 16KB, 32KB, 64KB, 128KB, and 1024KB
 *      4. Machine: Windows 10
 *      5. Measurement type: Using the System.nanoTime() method
 *      6. Time was captured only after three ignored consecutive runs; the first three runs are ignored to achieve a trend.
 *         The first-time run is always slower than the subsequent runs.
 *
 *
 * Created by gxy on 2016/6/24.
 */
public class BenchmarkingFileChannelCopyCapabilities {

    public static void deleteCopied(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Path copy_from = Paths.get("D:\\source\\movie\\test.mp4");
        final Path copy_to = Paths.get("E:\\tmp\\test.mp4");
        long startTime, elapsedTime;
        int bufferSizeKB = 4; //also tested for 16, 32, 64, 128, 256 and 1024
        int bufferSize = bufferSizeKB * 1024;
        deleteCopied(copy_to);
        //FileChannel and non-direct buffer
        System.out.println("Using FileChannel and non-direct buffer ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
                     FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {
            startTime = System.nanoTime();

            // Allocate a non-direct ByteBuffer
            ByteBuffer bytebuffer = ByteBuffer.allocate(bufferSize);
            // Read data from file into ByteBuffer
            int bytesCount;
            while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
                //flip the buffer which set the limit to current position, and position to 0
                bytebuffer.flip();
                //write data from ByteBuffer to file
                fileChannel_to.write(bytebuffer);
                //for the next read
                bytebuffer.clear();
            }

            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);
        //FileChannel and direct buffer
        System.out.println("Using FileChannel and direct buffer ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {
            startTime = System.nanoTime();

            // Allocate a direct ByteBuffer
            ByteBuffer bytebuffer = ByteBuffer.allocateDirect(bufferSize);
            // Read data from file into ByteBuffer
            int bytesCount;
            while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
                //flip the buffer which set the limit to current position, and position to 0
                bytebuffer.flip();
                //write data from ByteBuffer to file
                fileChannel_to.write(bytebuffer);
                //for the next read
                bytebuffer.clear();
            }

            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);
        //FileChannel.transferTo()
        System.out.println("Using FileChannel.transferTo method ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {
            startTime = System.nanoTime();
            fileChannel_from.transferTo(0L, fileChannel_from.size(), fileChannel_to);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);

        //FileChannel.transferFrom()
        System.out.println("Using FileChannel.transferFrom method ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {
            startTime = System.nanoTime();
            fileChannel_to.transferFrom(fileChannel_from, 0L, (int) fileChannel_from.size());
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);

        //FileChannel.map
        System.out.println("Using FileChannel.map method ...");
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

            startTime = System.nanoTime();
            MappedByteBuffer buffer = fileChannel_from.map(FileChannel.MapMode.READ_ONLY,
                    0, fileChannel_from.size());

            fileChannel_to.write(buffer);
            buffer.clear();
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }

        deleteCopied(copy_to);

        //Buffered Stream I/O
        System.out.println("Using buffered streams and byte array ...");
        File inFileStr = copy_from.toFile();
        File outFileStr = copy_to.toFile();
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr))) {
            startTime = System.nanoTime();
            byte[] byteArray = new byte[bufferSize];
            int bytesCount;
            while ((bytesCount = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, bytesCount);
            }
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);
        System.out.println("Using un-buffered streams and byte array ...");
        try (FileInputStream in = new FileInputStream(inFileStr);
             FileOutputStream out = new FileOutputStream(outFileStr)) {
            startTime = System.nanoTime();
            byte[] byteArray = new byte[bufferSize];
            int bytesCount;
            while ((bytesCount = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, bytesCount);
            }
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        deleteCopied(copy_to);
        System.out.println("Using Files.copy (Path to Path) method ...");
        try {
            startTime = System.nanoTime();
            Files.copy(copy_from, copy_to, LinkOption.NOFOLLOW_LINKS);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException e) {
            System.err.println(e);
        }
        deleteCopied(copy_to);
        System.out.println("Using Files.copy (InputStream to Path) ...");
        try (InputStream is = new FileInputStream(copy_from.toFile())) {
            startTime = System.nanoTime();
            Files.copy(is, copy_to);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException e) {
            System.err.println(e);
        }
        deleteCopied(copy_to);
        System.out.println("Using Files.copy (Path to OutputStream) ...");
        try (OutputStream os = new FileOutputStream(copy_to.toFile())) {
            startTime = System.nanoTime();
            Files.copy(copy_from, os);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
