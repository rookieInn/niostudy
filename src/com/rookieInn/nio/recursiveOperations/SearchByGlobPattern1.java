package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * Created by gxy on 2016/6/16.
 */
public class SearchByGlobPattern1 implements FileVisitor {

    private final PathMatcher matcher;
    private final long accepted_size;

    public SearchByGlobPattern1(String glob, long accepted_size) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        this.accepted_size = accepted_size;
    }

    public void search(Path file) throws IOException {
        Path name = file.getFileName();
        long size = (Long) Files.getAttribute(file, "basic:size");
        if (name != null && matcher.matches(name) && size <= accepted_size ) {
            System.out.println("Searched file was found: " + name + " in " + file.toRealPath().toString() + "size");
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        search((Path)file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        //report an error if necessary
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        System.out.println("Visited: " + (Path) dir);
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        String glob = "*.txt";
        long size = 102400; //100 kilobytes in bytes
        Path fileTree = Paths.get("E:/tmp");
        SearchByGlobPattern1 walk = new SearchByGlobPattern1(glob, size);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
    }
}
