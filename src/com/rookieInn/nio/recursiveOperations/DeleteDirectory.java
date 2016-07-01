package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * File Delete Application
 *
 * Deleting a single file is a simple operation, as you saw in the Deleting Files and Directories.
 * After call the delete() or deleteIfExists() method, the file deleted from your file system.
 * Deleting an entire file tree is an operation based on calling the delete() or deleteIfExists() method recursively through a FileVisitor implementation.
 * Before you see an example, here are a few things you need to keep in mind:
 *      a. Before you delete a directory, you must delete all file from it.
 *      b. The visitFile() method is the best place to perform the deletion of each file.
 *      c. Since you can delete a directory only if it is empty, it is recommended to delete directories in the postVisitDirectory() method.
 *      d. If a file cannot be visited, the visitFileFailed() method should return FileVisitResult.COUNT or TERMINATE, depending on your decision.
 *      e. The delete process can follow symbolic links, which may be not advisable, since symbolic links may point files outside the deletetion domain.
 *         But if you are sure that this case can never happen, or a supplementary condition prevents undesirable deletions, then follow symbolic links.
 *
 * Created by gxy on 2016/6/16.
 */
public class DeleteDirectory implements FileVisitor {

    public boolean deleteFileByFile(Path file) throws IOException {
        return Files.deleteIfExists(file);
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        boolean success = deleteFileByFile((Path) file);
        if (success)
            System.out.println("Deleted: " + (Path) file);
        else
            System.out.println("Not deleted: " + (Path) file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        //report an error if necessary
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        if (exc == null) {
            System.out.println("Visited: " + (Path) dir);
            boolean success = deleteFileByFile((Path) dir);
            if (success)
                System.out.println("Deleted: " + (Path) dir);
            else
                System.out.println("Not deleted: " + (Path) dir);
        } else {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("E:/tmp");
        DeleteDirectory walk = new DeleteDirectory();
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(dir, opts, Integer.MAX_VALUE , walk);
    }
}
