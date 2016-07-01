package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * Created by gxy on 2016/6/16.
 */
public class SearchByGlobPattern implements FileVisitor {

    private final PathMatcher matcher;

    public SearchByGlobPattern(String glob) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    public void search(Path file) throws IOException {
        Path name = file.getFileName();
        if (name != null && matcher.matches(name)) {
            System.out.println("Searched file was found: " + name + " in " + file.toRealPath().toString());
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

    public static void main(String[] args) {
        String glob = "*.jpg";
        Path fileTree = Paths.get("E:/tmp");
        SearchByGlobPattern walk = new SearchByGlobPattern(glob);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        try {
            Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
