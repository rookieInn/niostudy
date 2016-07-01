package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.file.*;
import java.util.EnumSet;

/**
 * The SimpleFileVisitor Class
 *
 * Implementing the FleVisitor interface requires implementing all of its methods, which may be undesirable if you need to implement only one or a few of those methods.
 * In that case, it it much simple to extend the SimpleFileVisitor class, which implements the FileVisitor interface.
 * This approach requires overwriting only the desired methods.
 *
 * Created by gxy on 2016/6/16.
 */
public class ListTree extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.println("Visited directory: " + dir.toString());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.println(exc);
        return FileVisitResult.CONTINUE;
    }

    /*
      Starting the Recursive Process
      Once you have created the recursive mechanism(by implementing the FileVisitor interface or extending the SimpleFileVisitor class),
      you can start the process vy calling one of the two Files.walkFileTree() methods.
      This simplest walkFileTree() method gets the starting file (this is usually the file tree root) and the file visitor to invoke for each file (this is an instance of the recursive mechanism class).
      For example, you can start the code example in the preceding section by calling walkFileTree() method as follows:
     */
    public static void main(String[] args) {
        Path listDir = Paths.get("E:/"); //define the starting file tree
        ListTree walk = new ListTree();
/*        try {
            Files.walkFileTree(listDir, walk); //start the walk
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        The second walkFileTree() method gets the starting file, options to customize the walk, the maximum number of director levels to visit
        (to ensure that all levels are traversed, you can specify Integer.Max_VALUE for the maximum depth argument), and the walk instance.
        The accepted options are the constants of the FileVisitOption enum.
        Actually, this enum contains a single constant, named FOLLOW_LINK, indicating that the symbolic links are followed in the walk (by default, they are not followed).
        */
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS); //follow links

        try {
            Files.walkFileTree(listDir, opts, Integer.MAX_VALUE, walk); //start the walk
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
