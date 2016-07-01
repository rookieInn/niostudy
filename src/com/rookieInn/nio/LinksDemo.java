package com.rookieInn.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Set;

/**
 * Links
 *
 * differences/similarities between the two types of links:
 * 1. Hard links can be created only for files, not for directions.
 *    Symbolic link to a file or a directory.
 * 2. Hard links cannot exist across file systems.
 *    Symbolic links can exist across file systems.
 * 3. The target of a hard link must exist.
 *    The target of a symbolic link may not exist.
 * 4. Removing the original file that your hard link points to does not remove the hard link itself, and the hard link still provides the content of the underlying file.
 *    Removing the original file that your symbolic link points to does not remove the attached symbolic link, but without the original file, the symbolic link is useless.
 * 5. If you remove the hard link or the symbolic link itself, the original file stays intact.
 * 6. A hard link is the same entity as the original file.
 *    All attributes are identical.
 *    A symbolic link is not so restrictive.
 * 7. A hard link looks, and behaves, like a regular file, so hard links can be find.
 *    A symbolic link's target may not even exist, therefore it is much flexible.
 *
 * Create Links from the Command Line
 * Windows users can create symbolic and hard links from the command line by using the mklink command.
 * This command gets a set of options, depending on which kind of link you need to create.
 * Some of these options are as follows:
 * /D       Creates a directory symbolic link. Default is a file symbolic link.
 * /H       Creates a hard link instead of a symbolic link.
 * /J       Creates a Directory Junction.
 * Link     specifies the new symbolic link name.
 * Target   specifies the path (relative or absolute) that the new link refers to.
 * For instance, if you want make the folder C:\rafaelnadal\photos available from C:\rafaelnadal as well, you could use the following command:
 * mklink /D C:\rafaelnadal C:\rafaelnadal\photos
 * Now if you look in the C:\rafaelnadal directory, you'll also see whatever files were in the C:\rafaelnadal\photos directory.
 * Unix(Linux) users can use the command named ln to achieve the same effect achieved in the preceding Windows example (notice that the target file is listed first and the link name is listed second this case):
 * ln -s /home/rafaelnadal/photos /home/rafaelnadal
 * In addition, in Unix(Linux) you can delete a link using the rm command:
 * rm /home/rafaelndal
 *
 *
 * Created by gxy on 2016/6/10.
 */
public class LinksDemo {

    /**
     * Creating a Symbolic Link
     */
    @Test
    public void creatingSymbolicLink() {
        Path link = FileSystems.getDefault().getPath("rookieInn.123");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When you want to modify the default attributes of the link, you can use the third argument of the createSymbolicLink() method.
     * This argument is an array of attributes of type FileAttribute—the class that encapsulates the value of a file attribute that can be set atomically when creating a new file directory, or link.
     * The following code snippet reads the attributes of the target file and creates a link,assigning the attributes from the target to the link.
     * It creates a symbolic link namedrafael.nadal.2 for file C:\rafaelnadal\photos\rafa_winner.jpg (the file must exist and the file system must have permission to create symbolic links).
     */
    @Test
    public void creatingSymbolicLink1() {
        Path link = FileSystems.getDefault().getPath("rookieInn.1");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            PosixFileAttributes attrs = Files.readAttributes(target, PosixFileAttributes.class);
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(attrs.permissions());
            Files.createSymbolicLink(link, target, attr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * In addition you can use the setAttribute() method to modify the link attributes after creation.
     * For example, the following code snippet reads the lastModifiedTime and lastAccessTime attributes of the target and sets them to link.
     * It creates a symbolic link named rafael.nadal.3 for file C:\rafaelnadal\photos\rafa_winner.jpg
     */
    @Test
    public void creatingSymbolicLink2() {
        Path link = FileSystems.getDefault().getPath("rookieInn.3");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Files.createSymbolicLink(link, target);
            FileTime lm = (FileTime) Files.getAttribute(target, "basic:lastModifiedTime", LinkOption.NOFOLLOW_LINKS);
            FileTime la = (FileTime) Files.getAttribute(target, "basic:lastAccessTime", LinkOption.NOFOLLOW_LINKS);

            Files.setAttribute(link, "basic:lastModifiedTime", lm, LinkOption.NOFOLLOW_LINKS);
            Files.setAttribute(link, "basic:lastAccessTime", la, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a Hard Link
     */
    @Test
    public void createAHardLink() {
        Path link = FileSystems.getDefault().getPath("rookieInn.4");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Files.createLink(link, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkingASymbolicLink() {
        Path link = FileSystems.getDefault().getPath("rookieInn.5");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean link_isSymbolicLink_1 = Files.isSymbolicLink(link);
        boolean target_isSymbolicLink_1 = Files.isSymbolicLink(target);

        System.out.println(link.toString() + " is a symbolic link ? " + link_isSymbolicLink_1);
        System.out.println(target.toString() + " is a symbolic link ? " + target_isSymbolicLink_1);
        //Another way:

        try {
            boolean link_isSymbolicLink_2 = (boolean) Files.getAttribute(link, "basic:isSymbolicLink");
            boolean target_isSymbolicLink_2 = (boolean) Files.getAttribute(target, "basic:isSymbolicLink");
            System.out.println(link.toString() + " is a symbolic link ? " + link_isSymbolicLink_2);
            System.out.println(target.toString() + " is a symbolic link ? " + target_isSymbolicLink_2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Locating the Target of a Link
     */
    @Test
    public void locatingTargetOfALink() {
        Path link = FileSystems.getDefault().getPath("rookieInn.6");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Path linkedpath = Files.readSymbolicLink(link);
            System.out.println(linkedpath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking If a Link and a Target Point to the Same File
     */
    @Test
    public void checkingIfALinkAndATargetPointToTheSameFile() {
        Path link = FileSystems.getDefault().getPath("rookieInn.7");
        Path target = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            Files.createSymbolicLink(link, target);
            boolean isSameFile = Files.isSameFile(link, target);
            if (isSameFile) {
                System.out.println(link.toString() + " and " + target.toString() + " point the same location");
            } else {
                System.out.println(link.toString() + " and " + target.toString() + " don't point the same location");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
