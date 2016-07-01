package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Common Walk
 * There is a set of common walks that you can easily implement through the FileVisitor interface.
 * This section shows you how to write and implement applications to perform a file search, a recursive copy, a recursive move, and a recursive delete.
 *
 * Writing a File Search Application
 * Most OS provide a dedicated tool for searching files(for example, Linux has the find command, while Windows has the File Search tool).
 * For simple searches to advanced searches, all of the tools generally work in the same way:
 * you specify the search criteria and then wait for the tool to find the matching file(s).
 * But, if you need to accomplish the search programmatically, then FileVisitor can help you with the traversal process.
 * Whether you are looking for a file by name, by extension, or by a glob pattern or are looking inside files from text or code, the approach is always to visit each file in the file store and perform some checks to determine whether the file conforms to your search criteria.
 *      When you write you file search tool based on FileVisitor, you need to keep in mind the following:
 *          a. The visitFile() method is the best place to perform the comparison between the current file and you search criteria.
 *             At this point you can extract each file name, its extension, or its attributes or open the file reading.
 *             You can use the file name, extension, and so on for determining whether the visited file is the searched one.
 *             Sometimes you will mix these information into complex search criteria.
 *             This method does not find directories.
 *          b. If you want to find directories, then the comparison must take place in the preVisitDirectory() or postVisitDirectory() method, depending on case.
 *          c. If a file cannot be visited, the visitFileFailed() method should return FileVisitResult.CONTINUE because this issue does not require the entire search process to be stopped.
 *          d. If you search for a file by name and you know that there is a single file with that name in the file tree, then you can return FileVisitResult.TERMINATE once the visitFile() method finds it.
 *             Otherwise, FileVisitResult.CONTINUE should be returned.
 *          e. The search process can follow symbolic links, which can be a good idea, since following symbolic links may locate the searched file before traversing the symbolic link's target sbu-tree.
 *             Following symbolic links is not always a good idea;
 *             for example, for deleting files it is not advisable.
 *
 *
 *
 * Created by gxy on 2016/6/16.
 */
public class Search implements FileVisitor {

    private final Path searchedFile;
    public boolean found;

    public Search(Path searchedFile) {
        this.searchedFile = searchedFile;
        this.found = false;
    }

    void search(Path file) throws IOException {
        Path name = file.getFileName();
        if (name != null && name.equals(searchedFile)) {
            System.out.println("Searched file was found :" + searchedFile + " in " + file.toRealPath().toString());
            found = true;
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        search((Path) file);
        if(!found)
            return FileVisitResult.CONTINUE;
        else
            return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        //report an error if necessary
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        System.out.println("Visited: " + (Path)dir);
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path searchFile = Paths.get("wiki.txt");
        Search walk = new Search(searchFile);
        EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        Path path = Paths.get("E:/");
        if (!walk.found)
            try {
                Files.walkFileTree(path, opts, Integer.MAX_VALUE, walk);
            } catch (IOException e) {
                e.printStackTrace();
            }
/*        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        for ( Path root : dirs) {
            if (!walk.found)
                Files.walkFileTree(root, opts, Integer.MAX_VALUE, walk);
        }
        if (!walk.found) {
            System.out.println("The file " + searchFile + " was not found!");
        }*/
    }
}
