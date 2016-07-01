package com.rookieInn.nio;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Working with the Path Class
 * Created by gxy on 2016/6/8.
 */
public class PathDemo {

    /**
     * define a path
     */
    @Test
    public void definePath() {
        String filepath = "file:///C:/Users/gxy/Desktop/Introduction_20151027.pdf";
        //define an absolute path
        Path path00 = Paths.get("C:/Users/gxy/Desktop/Introduction_20151027.pdf");
        Path path01 = Paths.get("C:", "Users/gxy/Desktop", "Introduction_20151027.pdf");
        Path path02 = Paths.get("C:", "Users", "gxy/Desktop", "Introduction_20151027.pdf");

        //difine a path relative to the file store root
        Path path03 = Paths.get("/gxy/Desktop/Introduction_20151027.pdf");
        Path path04 = Paths.get("Users", "gxy/Desktop/Introduction_20151027.pdf");

        //define a path relative to the working folder
        Path path05 = Paths.get("nio/123.txt");

        //define a path using shortcuts
        Path path06 = Paths.get("C:/Users/.../Desktop/Introduction_20151027.pdf").normalize();

        //define the path of the home directory
        Path path07 = Paths.get(URI.create(filepath));

        //get the path of the home directory
        Path path08 = Paths.get(System.getProperty("user.home"), "downloads", "GitHubSetup.exe");

        System.out.println(path00.getFileName());
        System.out.println(path01.getFileName());
        System.out.println(path02.getFileName());
        System.out.println(path03.getFileName());
        System.out.println(path04.getFileName());
        System.out.println(path05.getFileName());
        System.out.println(path06.getParent());
        System.out.println(path07.getFileName());
        System.out.println(path08.getParent());

    }

    /**
     * getting information about a path
     */
    @Test
    public void getPathInformation() {
        Path path = Paths.get("E:/idea/nio/123.txt");
        //get the path / directory name
        System.out.println("The file/directory indicated by path:" + path.getFileName());
        //get path root
        System.out.println("Root of this path:" + path.getRoot());
        //get the path parent
        System.out.println("Parent:" + path.getParent());
        //get path name elements
        System.out.println("Number of name elements is path:" + path.getNameCount());
        for (int i =0; i < path.getNameCount(); i++) {
            System.out.println("Name element" + i + " is:" + path.getName(i));
        }
        //get a path subpath
        System.out.println("Subpath(0,3): " + path.subpath(0, 2));
    }

    /**
     * convert a path
     */
    @Test
    public void convertPath() {
        Path path = Paths.get("C:/Users/gxy/Desktop", "Introduction_20151027.pdf");
        //convert a path to a string
        System.out.println("Path to string:" + path.toString());
        //convert a path to a uri
        System.out.println("Path to URI:" + path.toUri());
        //convert a relative path to an absolute path
        System.out.println("Path to absolute path:" + path.toAbsolutePath());
        //convert a path to a real path
        try {
            Path real_path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
            System.out.println("Path to real path:" + real_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert a path to a file
        File path_to_file = path.toFile();
        Path file_to_path = path_to_file.toPath();
        System.out.println("Path to file name:" + path_to_file.getName());
        System.out.println("File to path:" + file_to_path.toString());
    }

    /**
     * Converting a Path
     */
    @Test
    public void convertingPath() {
        //define the fixed path
        Path base = Paths.get("E:/idea/nio");

        //resolve 123.txt file
        Path path_1 = base.resolve("123.txt");
        System.out.println(path_1.toString());

        Path path_2 = base.resolve("456.txt");
        System.out.println(path_2.toString());

        //combining two path use sibling()
        //define the fixed path
        Path base1 = Paths.get("E:/idea/nio/123.txt");

        //resolve sibling 456.txt file
        Path path_3 = base1.resolveSibling("456.txt");
        System.out.println(path_3.toString());
    }

    /**
     * Constructing a Path Between Two Locations
     */
    @Test
    public void constructPathBetweenTwoLocations() {
        //the follow two paths are relative paths
        Path path01 = Paths.get("123.txt");
        Path path02 = Paths.get("456.txt");
        //123.txt and 456.txt are siblings.
        Path path01_to_path02 = path01.relativize(path02);
        System.out.println(path01_to_path02);

        Path path02_to_path01 = path02.relativize(path01);
        System.out.println(path02_to_path01);


        //Another typical situation involves two paths that contain a root element.
        //Consider the following path:
        Path path03 = Paths.get("/idea/nio/123.txt");
        Path path04 = Paths.get("/idea/nio");

        Path path03_to_path04 = path03.relativize(path04);
        System.out.println(path03_to_path04);

        Path path04_to_path03 = path04.relativize(path03);
        System.out.println(path04_to_path03);
    }

    /**
     * Comparing Two Paths
     */
    @Test
    public void compareTwoPaths() {
        Path path01 = Paths.get("E:/idea/nio/123.txt");
        Path path02 = Paths.get("/idea/nio/123.txt");

        if (path01.equals(path02)) {
            System.out.println("The paths are equal!");
        } else {
            System.out.println("The path are not equal!"); //true
        }

        // use Files.isSameFile()
        try {
            boolean check = Files.isSameFile(path01, path02);
            if(check) {
                System.out.println("The paths locate the same file!"); //true
            } else {
                System.out.println("The paths does not locate the same file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Path class implements the Comparable interface, you can compare paths by using the compareTo() method.
        int compare = path01.compareTo(path02);
        System.out.println(compare);

        boolean sw = path02.startsWith("/idea/nio");
        boolean ew = path01.endsWith("123.txt");
        System.out.println(sw);
        System.out.println(ew);
    }

    /**
     * Iterate over the Name Elements of a Path
     */
    @Test
    public void iterateNameElementOfPath() {
        Path path = Paths.get("E:/idea/nio/123.txt");

        for (Path name: path) {
            System.out.println(name);
        }
    }

}
