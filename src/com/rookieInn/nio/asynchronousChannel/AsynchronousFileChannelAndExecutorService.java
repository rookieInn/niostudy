package com.rookieInn.nio.asynchronousChannel;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * AsynchronousFileChannel and ExecutorService
 *
 * Created by gxy on 2016/6/30.
 */
public class AsynchronousFileChannelAndExecutorService {

    public static void main(String[] args) {
        final int THREADS = 5;
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        String encoding = System.getProperty("file.encoding");
        List<Future<ByteBuffer>> list = new ArrayList<>();
        int sheeps = 0;

        Path path = Paths.get("E:/idea/nio", "story.txt");
        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, withOptions(), executor)) {
            for (int i = 0; i < 50; i++) {
                Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {
                    @Override
                    public ByteBuffer call() throws Exception {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
                        channel.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));
                        return buffer;
                    }
                };
                list.add(executor.submit(worker));
            }

            //this will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();

            //wait until all threads are finished
            while(!executor.isTerminated()) {
                //do something else while the buffers are prepared
                System.out.println("Counting sheep while filling up some buffers! So fa I counted: " + (sheeps += 1));
            }

            System.out.println("\nDone! Here are the buffers:\n");
            for (Future<ByteBuffer> future : list) {
                ByteBuffer buffer = future.get();

                System.out.println("\n\n" + buffer);
                System.out.println("__________________________________________________________");
                buffer.flip();
                System.out.println(Charset.forName(encoding).decode(buffer).toString());
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set withOptions() {
        final Set options = new TreeSet<>();
        options.add(StandardOpenOption.READ);
        return options;
    }

}
