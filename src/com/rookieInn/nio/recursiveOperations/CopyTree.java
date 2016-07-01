package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

/**
 * Writing a Copy Files Application
 *
 * Copying a file tree requires calling the Files.copy() method for each traversed file and directory.
 *      a. Before you copy any files from a directory, you must copy the directory itself.
 *         Copying a source directory (empty or not) will result in an empty target directory.
 *         This task must be accomplished in the preVisitDirectory() method.
 *      b. The visitFile() method is the perfect place to copy each file.
 *      c. When you copy a file or directory, you need to decide whether or not you want to use the REPLACE_EXISTING and COPY_ATTRIBUTES options.
 *      d. If you want to preserve the attributes of the source directory, you need to do that after have been copied, in the postVisitDirectory() method.
 *      e. If you choose to follow link(FOLLOW_LINK) and your file tree has a circular link to a parent directory, the looping directory is reported in the visitFileFailed() method with the FileSystemLoopException exception.
 *      f. If a file cannot be visited, the visitFileFailed() method should return FileVisitResult.CONTINUE or TERMINATE, depending on your decision.
 *      g. The copy process can follow symbolic links if you specify the FOLLOW_LINKS option.
 *
 *
 * Created by gxy on 2016/6/16.
 */
public class CopyTree implements FileVisitor{

    private final Path copyFrom;
    private final Path copyTo;

    public CopyTree(Path copyFrom, Path copyTo) {
        this.copyFrom = copyFrom;
        this.copyTo = copyTo;
    }

    public static void copySubTree(Path copyFrom, Path copyTo) {
        try {
            Files.copy(copyFrom, copyTo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.err.println("Unable to copy " + copyFrom + "[" + e + "]");
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("Copy directory: " + (Path) dir);
        Path newdir = copyTo.resolve(copyFrom.relativize((Path) dir));
        try {
            Files.copy((Path) dir, newdir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException E) {
            System.err.println("Unable to create " + newdir + "[" + newdir + "]");
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        System.out.println("Copy file: " + (Path) file);
        copySubTree((Path) file, copyTo.resolve(copyFrom.relativize((Path) file)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        if (exc instanceof FileSystemLoopException)
            System.err.println("Cycle was detected: " + (Path) file);
        else
            System.out.println("Error occurred, unable to copy:" + (Path)file + "[" + exc + "]");
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        if (exc == null) {
            Path newdir = copyTo.resolve(copyFrom.relativize((Path) dir));
            try {
                FileTime time = Files.getLastModifiedTime((Path) dir);
                Files.setLastModifiedTime(newdir, time);
            } catch (IOException e) {
                System.err.println("Unable to copy all attributes to: " + newdir + "[" + e + "]");
            }
        } else
            throw exc;
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path copyFrom = Paths.get("E:/idea/nio");
        Path copyTo = Paths.get("E:/tmp");

        CopyTree walk = new CopyTree(copyFrom, copyTo);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(copyFrom, opts, Integer.MAX_VALUE, walk);
    }

}
