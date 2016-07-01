package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

/**
 * Writing a Move File Application
 *
 * Moving a file tree is a task that combines into a single application the steps of copying and deleting the file tree.
 * Actually, there are two approaches commonly used to move a file tree:
 * combine Files.move(), Files.copy(), and Files.delete(), or use only Files.copy() and Files.delete().
 * Depending on the approach you choose, FileVisitor should be implemented accordingly to accomplish the move file tree task.
 *      a. Before you move any files from a directory, you must move the directory itself.
 *         Since nonempty directories cannot be moved (only empty directories can be moved),
 *         you need to use the Files.copy() method, which will copy an empty directory instead.
 *         This task must be accomplished in the preVisitDirectory() method.
 *      b. The visitFile() method is the perfect place to move each file. For this you can use the Files.move() method, or Files.copy() combined with Files.delete().
 *      c. After all files from a source directory are moved into the target directory, you need to call Files.delete() to delete the source directory, which, at this moment, should be empty.
 *         This task mush be accomplished in the postVisitDirectory() method.
 *      d. When you copy a file or directory, you need to decide whether or not you want to use the REPLACE_EXISTING and COPY_ATTRIBUTES option.
 *         Moreover, when you move a file or directory, you need to decide if ATOMIC_MOVE is needed.
 *      e. If you want to preserve the attributes of the source directory, you need to do that after the files have been moved, in the postVisitDirectory() method.
 *         Some attributes, such as lastModifiedTime, should be extracted in the postVisitDirectory().
 *         The reason is that after you move a file from the source directory, the directory content has changed and the initial last modified time is overwritten by the new date.
 *      f. If a file cannot be visited, the visitFileFailed() method should return FileVisitResult.CONTINUE or TERMINATE, depending on your decision.
 *      g. The move process can follow symbolic links if you specify the FOLLOW_LINKS option.
 *         Keep in mind that moving a symbolic link moves the link itself, not target of that link.
 *
 * Created by gxy on 2016/6/16.
 */
public class MoveTree implements FileVisitor {
    private final Path moveFrom;
    private final Path moveTo;
    static FileTime time = null;

    public MoveTree(Path moveFrom, Path moveTo) {
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public static void moveSubTree1(Path moveFrom, Path moveTo) throws IOException {
        try {
            Files.copy(moveFrom, moveTo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            Files.delete(moveFrom);
        } catch (IOException e) {
            System.err.println("Unable to move " + moveFrom + " [" + e + "]");
        }
    }

    public void moveSubTree(Path moveFrom, Path moveTo) {
        try{
            Files.move(moveFrom, moveTo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.err.println("Unable to move " + moveFrom + "[" + e + "]");
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        //复制文件夹
        System.out.println("Move directory: " + (Path)dir);
        Path newdir = moveTo.resolve(moveFrom.relativize((Path)dir));
        try {
            Files.copy((Path)dir, newdir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            time = Files.getLastModifiedTime((Path)dir);
        } catch (IOException e) {
            System.err.println("Unable to move " + newdir + "[" + e + "]");
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        System.out.println("Move file: " + (Path) file);
        //剪切文件
        moveSubTree((Path) file, moveTo.resolve(moveFrom.relativize((Path)file)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        //continue
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        System.out.println("Delete directory: "  + (Path)dir);
        //删除文件家
        Path newdir = moveTo.resolve(moveFrom.relativize((Path)dir));
        try{
            Files.setLastModifiedTime(newdir, time);
            Files.delete((Path) dir);
        } catch (IOException e) {
            System.err.println("Unable to copy all attributes to: " + newdir + " [" + e + "]" );
        }
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path moveFrom = Paths.get("E:/temp");
        Path moveTo = Paths.get("E:/tmp");
        MoveTree walk = new MoveTree(moveFrom, moveTo);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(moveFrom, opts, Integer.MAX_VALUE, walk);
    }

}
