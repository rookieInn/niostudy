package com.rookieInn.nio;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by gxy on 2016/6/10.
 */
public class FileAndDirectories {

    /* Checking Methods for Files and Directories */
    /**
     * Checking for the Existence of a File or Directory
     * Files class provides the following two methods for this type of check:
     * exists(): Checks whether a file exists
     * notExists(): Checks whether a file does not exist
     */
    @Test
    public void checkingForTheExistenceOfAFileOrDirectory() {
        Path path = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        System.out.println(Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}));
        System.out.println(!Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}));
    }

    /**
     * Checking File Accessibility
     * isReadable()
     * isWritable()
     * isExecutable()
     * isRegularFile()
     */
    @Test
    public void checkingFileAccessibility() {
        Path path = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");

        boolean is_readable = Files.isReadable(path);
        boolean is_writable = Files.isWritable(path);
        boolean is_executable = Files.isExecutable(path);
        boolean is_regular = Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);

        if ((is_readable) && (is_writable) && (is_executable) && (is_regular)) {
            System.out.println("The checked file is accessible!");
        } else {
            System.out.println("The checked file is not accessible!");
        }

        //Or
        boolean is_accessible = Files.isRegularFile(path) & Files.isReadable(path) & Files.isExecutable(path) & Files.isWritable(path);
        if (is_accessible) {
            System.out.println("The checked file is accessible!");
        } else {
            System.out.println("The checked file is not accessible!");
        }
    }

    /**
     * Checking If Two Paths Point to the Same File
     * isSameFile()
     */
    @Test
    public void checkingIfTwoPathsPointToTheSameFile() {
        Path path_1 = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        Path path_2 = FileSystems.getDefault().getPath("/idea/nio/123.txt");
        Path path_3 = FileSystems.getDefault().getPath("E:/idea/javastudy/../nio/123.txt");
        try {
            boolean is_same_file_12 = Files.isSameFile(path_1, path_2);
            boolean is_same_file_13 = Files.isSameFile(path_1, path_3);
            boolean is_same_file_23 = Files.isSameFile(path_2, path_3);

            System.out.println("is same file 1&2 ? " + is_same_file_12);
            System.out.println("is same file 1&3 ? " + is_same_file_13);
            System.out.println("is same file 2&3 ? " + is_same_file_23);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking the File Visibility
     * Files.isHidden()
     */
    @Test
    public void checkingTheFileVisibility() {
        Path path = FileSystems.getDefault().getPath("E:/idea/nio/123.txt");
        try {
            System.out.println("Is hidden ? " + Files.isHidden(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Creating and Reading Directories */
    /**
     * Listing File System Root Directories
     */
    @Test
    public void listingFileSystemRootDirectories() {
        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
/*        for (Path name : dirs) {
            System.out.println(name);
        }*/
        //You can easily get from Iterable into an array as follows:
        ArrayList<Path> list = new ArrayList<>();
        for (Path name : dirs) {
            list.add(name);
        }
        Path[] arr = new Path[list.size()];
        list.toArray(arr);

        for (Path path : arr) {
            System.out.println(path);
        }

        //If you need to extract the file system root directories as an array of File, use the Java 1.6 solution:
        File[] roots = File.listRoots();
        for (File root : roots) {
            System.out.println(root);
        }
    }

    /**
     * Creating a New Directory
     * Files.createDirectory()
     */
    @Test
    public void creatingANewDirectory() {
        Path newdir = FileSystems.getDefault().getPath("E:/idea/nio/lib/");
        try {
            Path path = Files.createDirectory(newdir);
            System.out.println(path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //You can add a set of attributes at creation time as shown in the following example code snippet,
        //which creates a new directory on a POSIX file system that has specific permissions:
        Path newdir1 = FileSystems.getDefault().getPath("E:/idea/nio/lib/");
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            Files.createDirectory(newdir1, attr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * FilSystems.createDirectorys()
     */
    @Test
    public void createSequenceDirectory() {
        Path newdir = FileSystems.getDefault().getPath("E:/ttttt/ssss/libfsdfd/");
        try {
            Files.createDirectories(newdir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Listing a Directory's Content */
    /**
     * Listing the Entire Content
     * The following code snippet will return the entire contents of a directory as links, files, subdirectories, and hidden files
     */
    @Test
    public void listingTheEntireContent() {
        Path path =  Paths.get("E:/");
        //no filter applied
        System.out.println("\nNo filter applied:");
        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(path);
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listing the Content by Applying a Glob Pattern
     *
     * pattern rules:
     * 1. *: Represent (match) any number of characters, including none.
     * 2. **: Similar to *, but cross directories' boundaries.
     * 3. ?: Represent (match) exactly one character.
     * 4. {}: Represent a collection of subpatterns separated commas.
     *        For example, {A, B, C} matches A, B or C.
     * 5. []: Convey a set of single characters or a range of characters if the hyphen character is present.
     *        Some come common examples include the following:
     *        a. [0-9]: Matches any digit
     *        b. [A-Z]: Matches any uppercase letter
     *        c. [a-z, A-Z]: Matches any uppercase or lowercase letter
     *        d. [12345]: Matches any of 1, 2, 3, 4, or 5
     * 6. Within the square brackets, *, ?, and \ match themselves.
     * 7. All other characters match themselves.
     * 8. To match *, ?, or the other special characters, you can escape them by using the backslash character,\.
     *    For example, \\ matches a single backslash, and \ ? matches the question mark.
     */
    @Test
    public void listTheContentByApplyingAGlobPattern() {
        Path path = Paths.get("E:/idea/nio");
        //glob pattern applied
        System.out.println("\nGlob pattern applied:");
        try{
            DirectoryStream<Path> dir = Files.newDirectoryStream(path, "*.{txt, png}");
            for (Path file : dir) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List the Content by Applying a User-Defined Filter
     *
     * If a glob pattern does not satisfy you needs, then is time to write your own filter.
     * This is a simple task that requires implementing the DirectoryStream.Filter<T> interface, which has a single method, named accept().
     * A Path is accepted or rejected based on your implementation.
     * For example, the following code snippet accepts only directories in the final result:
     */
    @Test
    public void listTheContentByApplyingAUserDefinedFilter() {
        Path path = Paths.get("E:/");
        //user-defined filter - only directories are accepted
        DirectoryStream.Filter<Path> dir_filter = new DirectoryStream.Filter<Path>(){

            @Override
            public boolean accept(Path entry) throws IOException {
                return (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS));
            }

        };

        //Filter that accepts only files/directories larger than 200KB:
        DirectoryStream.Filter<Path> size_filter = new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return Files.size(path) > 204800L;
            }
        };

        //Filter that accepts only files modified in the current day:
        DirectoryStream.Filter<Path> time_filter = new DirectoryStream.Filter<Path>(){

            @Override
            public boolean accept(Path entry) throws IOException {
                long currentTime = FileTime.fromMillis(System.currentTimeMillis()).to(TimeUnit.DAYS);
                long modifiedTime = ((FileTime)Files.getAttribute(path, "basic:lastModifiesTime", LinkOption.NOFOLLOW_LINKS)).to(TimeUnit.DAYS);

                if (currentTime == modifiedTime) {
                    return true;
                }
                return false;
            }
        };

        //Filter that accepts only hidden files/directories:
        DirectoryStream.Filter<Path> hidden_filter = new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return !(Files.isHidden(path));
            }
        };

        //The created filter is next passed as a parameter to the newDirectoryStream() method:
        System.out.println("\nUser defined filter applied:");
        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(path, hidden_filter);
            for (Path file : ds) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Creating, Reading, and Writing Files */
    /** Using Standard Open Options */
    /*
        OpenOption
            |-- LinkOption
                NOFOLLOW_LINKS
            |-- StandardOpenOption
                READ                    Opens file for read access
                WRITE                   Opens file for write access
                CREATE                  Creates a new file if it does not exist
                CREATE_NEW              Creates a new file, failing with an exception if the file already exists
                APPEND                  Appends data to the end of the file (used with WRITE and CREATE)
                DELETE_ON_CLOSE         Delete the file when the stream is closed (used for deleting temporary files)
                TRUNCATE_EXISTING       Truncates the file to 0 bytes(used with WRITE option)
                SPARSE                  Causes the newly created file to be sparse
                SYNC                    Keeps the file content and metadata synchronized with the underlying storage device
                DSYNC                   Keeps the file content synchronized with the underlying storage device

         Some of these constants will be shown at work in the upcoming sections, after you take a look creating a new file.
     */

    /**
     * Create a New File
     * Files.createFile()
     */
    @Test
    public void createANewFile() {
        Path newfile = FileSystems.getDefault().getPath("E:/idea/nio/heheda.txt");
        try {
            Files.createFile(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //You can add a set of attributes at creation time as shown in the following coder snippet.
        //This code creates a new file on a POSIX file system that has specific permissions.
        Path newfile1 = FileSystems.getDefault().getPath("/home/rookieInn/123/haha/txt");
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            Files.createFile(newfile1, attr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this is not the only way to create a new file.
    }

    /* Writing a Small File */
    /**
     * Writing Bytes with the write() Method
     *
     * Writing bytes into a file can be accomplished with the Files.write() method.
     * This method gets the path to the file, the byte array with the bytes to write, and options specifying how the file is opened.
     * It returns the path of the written file.
     *
     * The following code snippet writes a byte array (representing a small tennis ball picture) with the default opening options
     * (the file name is ball.png and it will be written in the E:\idea\nio directory):
     */
    @Test
    public void writingBytesWithTheWriteMethod() {
       /* Path ball_path = Paths.get("E:/idea/nio/ball.png");
        byte[] ball_bytes = new byte[]{
                (byte)0x89,(byte)0x50,(byte)0x4e,(byte)0x47,(byte)0x0d,(byte)0x0a,(byte)0x1a,(byte)0x0a,
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0d,(byte)0x49,(byte)0x48,(byte)0x44,(byte)0x52,
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x10,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x10,
                (byte)0x08,(byte)0x02,(byte)0x00,
        (byte)0x49,(byte)0x45,(byte)0x4e,(byte)0x44,(byte)0xae,(byte)0x42,(byte)0x60,(byte)0x82
        };

        try {
            Files.write(ball_path, ball_bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //Now, if you check the corresponding path, you will find a small picture representing a tennis ball.
        //Moreover, if you need to write text(String) and you want to use this method, then convert the test to a byte array as follows
        //(the file name is wiki.txt and is created in E:\idea\nio):
        Path rf_wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        String rf_wiki =  "Rafael \"Rafa\" Nadal Parera (born 3 June 1986) is a Spanish professional tennis "
                + "player and a former World No. 1. As of 29 August 2011 (2011 -08-29)[update], he is ranked No. 2 "
                + "by the Association of Tennis Professionals (ATP). He is widely regarded as one of the greatest players "
                + "of all time; his success on clay has earned him the nickname\"The King of Clay\", and has prompted "
                + "many experts to regard him as the greatest clay court player of all time. Some of his best wins are:";
        byte[] rf_wiki_byte = new byte[0];
        try {
            rf_wiki_byte = rf_wiki.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Files.write(rf_wiki_path, rf_wiki_byte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Even if this works, it is much easier to use the write() method, described next, to write text wo files.
    }

    /**
     * Writing Lines with the write() Method
     *
     * Writing lines into a file can be accomplished by using the Files.write() method (a "line" is a char sequence).
     * After each line, the method appends the platform's line separator(line.separator system property).
     * This method gets the path to the file, an iterable object over the char sequence, a charset to use for encoding, and options specifying how the file is opend.
     * It returns the path to the written file.
     * The following code snippet writes some lines into a file(actually, it appends some lines to the end of the wiki txt)
     */
    @Test
    public void writingLinesWithTheWriteMethod() {
        Path rf_wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        Charset charset = Charset.forName("UTF-8");
        ArrayList<String> lines = new ArrayList<>();
        lines.add("\n");
        lines.add("Rome Masters - 5 titles in 6 years");
        lines.add("Monte Carlo Masters - 7 consecutive titles (2005-2011)");
        lines.add("Australian Open - Winner 2009");
        lines.add("Roland Garros - Winner 2005-2008, 2010, 2011");
        lines.add("Wimbledon - Winner 2008, 2010");
        lines.add("US Open - Winner 2010");

        try {
            Files.write(rf_wiki_path, lines, charset, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Reading a Small File */
    /**
     * Reading with the readAllBytes() Method
     * The Files.readAllBytes() method read the entire file into a byte array,
     * while the File.readAllLines() method reads the entire file into a collection of String (as described in the next section).
     * Focusing on the readAllBytes() method, the following code snippet reads the previously ball.png binary file
     * (the file must exist) into a byte array(the file path is passed as a argument)
     */
    @Test
    public void readingWithTheReadAllBytesMethod() {
       /*
        Path ball_path = Paths.get("E:/idea/nio/ball.png");
        try {
            byte[] ballArray = Files.readAllBytes(ball_path);
            //If you want to make sure that the returned byte array contains the picture, you can run (as a test) the following code snippet,
            //which writes the bytes into a file named bytes_to_ball.png in the same directory:
            Files.write(ball_path.resolveSibling("bytes_to_ball.png"), ballArray);
            //Or you can use the ImageIO as follows.
            //The line ImageIO.Write() will write your bufferedImage data to your disk as a file of type PNG and will store it in the E:/photo directory.
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(ballArray));
            ImageIO.write(bufferedImage, "png", (ball_path.resolveSibling("bytes_to_ball.png")).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        The readAllBytes() method can also read a text file.
        This time the byte array should be converted to string, as in the following example
        (you can use any charset that is proper for your text files)
         */
        Path wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        try {
            byte[] wikiArray  = Files.readAllBytes(wiki_path);
            String wikiString  = new String(wikiArray, "ISO-8859-1");
            System.out.println(wikiString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reading with the readAllLines() Method
     *
     * Conforming to official documentation, this method recognizes the following as line terminators:
     *      a. \u000D followed by \u000A: CARRIAGE RETURN followed by LINE FEED
     *      b. \u000A: LINE FEED
     *      c. \u000D: CARRIAGE RETURN
     */
    @Test
    public void readingWithTheReadAllLinesMethod() {
        Path wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        Charset charset = Charset.forName("ISO-8859-1");
        try {
            List<String> lines = Files.readAllLines(wiki_path);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Working with Buffered Streams */
    /**
     * Using the newBufferedWriter() Method
     * Files.newBufferedWriter()
     * The method opens the file for writing (this can involve creating the file, if it doesn't exist)
     * or initially truncates an existing regular file to a size of 0 bytes.
     * In short, this method acts as if the CREATE, TRUNCATE_EXISTING, and WRITE options are present
     * (which is applicable by default when no other options are specified).
     */
    @Test
    public void usingTheNewBufferedWriterMethod() {
        Path wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        Charset charset = Charset.forName("UTF-8");
        String text = "Vamos Rafa!";
        try {
            BufferedWriter writer = Files.newBufferedWriter(wiki_path, charset, StandardOpenOption.APPEND);
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Using the newBufferedReader() Method
     */
    @Test
    public void usingTheNewBufferedReaderMethod() {
        Path wiki_path = Paths.get("E:/idea/nio/wiki.txt");
        Charset charset = Charset.forName("UTF-8");
        try(BufferedReader reader = Files.newBufferedReader(wiki_path, charset)){
            String line = null;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Working with Unbuffered Stream */
    /**
     * Using the newOutputStream() Method
     */
    @Test
    public void usingTheNewOutputStreamMethod() {
        /*
        Path rn_racquet = Paths.get("E:/idea/nio/racquet.txt");
        String racquet = "Racquet: Babolat AeroPro Drive GT";

        byte data[] = racquet.getBytes();
        try(OutputStream out = Files.newOutputStream(rn_racquet)) {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        Moreover, if you decide that it is a better idea to use a buffered stream instead of the preceding code,
        a conversion based on the java.io API is recommended, such as shown in the following code:
         */
        Path rn_racquet = Paths.get("E:/idea/nio/racquet.txt");
        String string = "\nString: Babolat RPM Blast 16";
        try(OutputStream out = Files.newOutputStream(rn_racquet, StandardOpenOption.APPEND);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))){
            writer.write(string);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using the newInputStream() Method
     */
    @Test
    public void usingTheNewInputStreamMethod() {
        Path rn_racquet = Paths.get("E:/idea/nio/racquet.txt");
        /*
        int n;
        try(InputStream in = Files.newInputStream(rn_racquet)) {
            while((n = in.read()) != -1) {
                System.out.print((char)n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //use buffer
        /*
        int n;
        byte[] in_buffer = new byte[1024];
        try(InputStream in = Files.newInputStream(rn_racquet)) {
            while((n = in.read(in_buffer)) != -1) {
                System.out.println(new String(in_buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //use reader
        try (InputStream in = Files.newInputStream(rn_racquet);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /*
    Creating Temporary Directories and Files
        A temporary directory is a directory that stores temporary files.
        The location of the temporary directory depends on the operating system.
        In Windows, the temporary directory is set through the TEMP environment variable, usually C:\Temp, %Window%\Temp,
        or a temporary directory per user in local Settings \Temp.
        In Linux/Unix the global temporary directories are /temp and /var/tmp
    */
    /**
     * Creating a Temporary Directory
     * Creating a temporary directory in the default operating system location can be accomplished by calling the createTempDirectory() method with two parameters:
     * a prefix string to be used in generating the directory's name (it can be null) and an optional list of file attributes to set atomically when creating the directory.
     */
    @Test
    public void creatingTemporaryDirectory() {
        /*
        String tmp_dir_prefix = "nio_";

        try {
            //passing null prefix
            Path tmp_1 =Files.createTempDirectory(null);
            System.out.println("TMP: " + tmp_1.toString());

            //set a prefix
            Path tmp_2 = Files.createTempDirectory(tmp_dir_prefix);
            System.out.println("TMP: " + tmp_2.toString());
            //output:
            // TMP: C:\Users\gxy\AppData\Local\Temp\4723917411318011217
            // TMP: C:\Users\gxy\AppData\Local\Temp\nio_7931183840153218927
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        Going further, you can specify the default directory in which a temporary directory is created by calling another createTempDirectory() method.
        Besides the temporary directory prefix and optional list of attributes, this method also gets a Path representing the default directory for temporary directories.
        */
        Path basedir = FileSystems.getDefault().getPath("E:/tmp");
        String tmp_dir_prefix = "rookieInn_";
        //create a tmp directory in the base dir
        try {
            Path tmp = Files.createTempDirectory(basedir, tmp_dir_prefix);
            System.out.println("TMP: " + tmp.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //output:
        //TMP: E:\temp\rookieInn_9182069256781063100
    }

    /**
     * Deleting a Temporary Directory with Shutdown-Hook
     */
    @Test
    public void deleteTemporaryDirectoryWithShutdownHook() {
        final Path basedir = FileSystems.getDefault().getPath("E:/tmp/");
        final String tmp_dir_prefix = "rookieInn_";

        try {
            //create a tmp directory in the base dir
            final Path tmp_dir = Files.createTempDirectory(basedir, tmp_dir_prefix);
            System.out.println(tmp_dir.toString());

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    System.out.println("Deleting the temporary folder ...");

                    try(DirectoryStream<Path> ds = Files.newDirectoryStream(tmp_dir)) {
                        for (Path file : ds) {
                            Files.delete(file);
                        }
                        Files.delete(tmp_dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Shutdown-hook completed ...");
                }
            });

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleting a Temporary Directory with the deleteOnExit() Method
     */
    @Test
    public void deletingTemporaryDirectoryWithTheDeleteOnExitMethod() {
        Path basedir = FileSystems.getDefault().getPath("E:/tmp/");
        String tmp_dir_prefix = "rookieInn_";

        try {
            //create a tmp directory in the base dir
            Path tmp_dir = Files.createTempDirectory(basedir, tmp_dir_prefix);

            System.out.println(tmp_dir.toString());

            File asFile = tmp_dir.toFile();
            asFile.deleteOnExit();

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating Temporary Files
     *
     */
    @Test
    public void creatingTemporaryFiles() {
        String tmp_file_prefix = "rookieInn_";
        String tmp_file_suffix = ".txt";

        /*
        try {
            //passing null prefix/suffix
            Path tmp_1 = Files.createTempFile(null, null);
            System.out.println("TMP: " + tmp_1.toString());

            //set a prefix and a suffix
            Path tmp_2 = Files.createTempFile(tmp_file_prefix, tmp_file_suffix);
            System.out.println("TMP: " + tmp_2.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } */

        // use base dir
        Path basedir = FileSystems.getDefault().getPath("E:/tmp");
        try {
            //passing null prefix/suffix
            Path tmp_1 = Files.createTempFile(basedir, null, null);
            System.out.println("TMP: " + tmp_1.toString());

            //set a prefix and a suffix
            Path tmp_2 = Files.createTempFile(basedir, tmp_file_prefix, tmp_file_suffix);
            System.out.println("TMP: " + tmp_2.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleting a Temporary File with Shutdown-Hook
     */
    @Test
    public void deletingTemporaryFileWithShutdownHook() {
        Path basedir = FileSystems.getDefault().getPath("E:/tmp");
        String tmp_file_prefix = "rookieInn_";
        String tmp_file_suffix = ".txt";

        try{
            final Path tmp_file = Files.createTempFile(basedir, tmp_file_prefix, tmp_file_suffix);

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    System.out.println("Deleting the temporary file ...");

                    try {
                        Files.delete(tmp_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Shutdown hook completed ...");
                }
            });

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleting a Temporary File with the deleteOnExit() Method
     */
    @Test
    public void deleteATemporaryFileWithTheDeleteOnExitMethod() {
        Path basedir = FileSystems.getDefault().getPath("E:/tmp");
        String tmp_file_prefix = "rookieInn_";
        String tmp_file_suffix = ".txt";

        try {
           final Path tmp_file = Files.createTempFile(basedir, tmp_file_prefix, tmp_file_suffix);
            System.out.println(tmp_file.toString());

            File asFile = tmp_file.toFile();
            asFile.deleteOnExit();

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleting a Temporary File with the DELETE_ON_CLOSE
     */
    @Test
    public void deleteATemporaryFileWithDELETE_ON_CLOSE() {
        Path basedir = FileSystems.getDefault().getPath("E:/tmp");
        String tmp_file_prefix = "rookieInn_";
        String tmp_file_suffix = ".txt";
        Path tmp_file = null;

        try {
            tmp_file = Files.createTempFile(basedir, tmp_file_prefix, tmp_file_suffix);
            System.out.println(tmp_file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(OutputStream out = Files.newOutputStream(tmp_file, StandardOpenOption.DELETE_ON_CLOSE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleting a Temporary File with the DELETE_ON_CLOSE
     * Moreover, you can simulate a temporary file even without calling the createTempFile() method.
     * Simply define a file name, and use the DELETE_ON_CLOSE option in conjunction with the CREATE option,
     * as shown in the following snippet
     */
    @Test
    public void deleteATemporaryFileWithDELETE_ON_CLOSE1() {
        String tmp_file_prefix = "rookieInn_";
        String tmp_file_suffix = ".txt";
        Path tmp_file = null;
        tmp_file = FileSystems.getDefault().getPath("E:/tmp", tmp_file_prefix + "temporary" +tmp_file_suffix);

        try (OutputStream out = Files.newOutputStream(tmp_file, StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE);
              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

              //simulate some I/O operations over the temporary file by sleeping 10 seconds
              //when the time expires. the temporary file is deleted
              Thread.sleep(10000);
              //operations done
         } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }

        try(OutputStream out = Files.newOutputStream(tmp_file, StandardOpenOption.DELETE_ON_CLOSE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            //simulate some I/O operations over the temporary file by sleeping 10 seconds
            //when the time expires. the temporary file is deleted
            Thread.sleep(10000);
            //operations done
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Deleting, Copying, and Moving Directories and Files */
    /**
     * Deleting Files and Directories
     *
     * void Files.delete()
     * boolean Files.deleteIfExits()
     */
    @Test
    public void deletingFilesAndDirectories() {
        Path path = FileSystems.getDefault().getPath("E:/idea/nio/img", "1.jpg");

        //delete the file
        try {
            Files.delete(path);
        } catch (IOException |
                SecurityException e) {
            e.printStackTrace();
        }

        try {
            boolean success = Files.deleteIfExists(path);
            System.out.println("Delete status: " + success);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copying Files and Directories
     * NIO.2 provides three Files.copy() method to accomplish copying files and directories.
     * NIO.2 provides a set of options for controlling the copy process-the methods take a varargs argument represented by these options.
     * This options are provided under the StandardCopyOption and LinkOption enums and are listed here:
     *      a. REPLACE_EXISTING: If the copied file already exists, then it is replaced
     *         (in the case of a nonempty directory, a FileAlreadyExistsException is thrown).
     *         When dealing with a symbolic link, the target of the link is not copied; only the link is copied.
     *      b. COPY_ATTRIBUTES: Copy a file with its associated attributes(at least, the lastModifiedTime attribute is supported and copied).
     *      c. NOFOLLOW_LINK: Symbolic links should not be followed.
     * If you are not familiar with enum types, then you should know that they can be imported into applications as follows.
     * These are called static and can imports and can import any static fields or methods, not just fields from enum types.
     * import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
     * import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
     * import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
     */
    /**
     * Copying Between Two Paths
     */
    @Test
    public void copyingBetweenTwoPaths() {
        Path copy_from = Paths.get("E:/idea/nio/123.txt");
        Path copy_to = Paths.get("E:/tmp", copy_from.getFileName().toString());

        try {
            Files.copy(copy_from, copy_to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copying from an InputStream to a File
     */
    @Test
    public void copyingFromAnInputStreamFile() {
        Path copy_form = Paths.get("E:\\idea\\nio\\racquet.txt");
        Path copy_to = Paths.get("E:/tmp", copy_form.getFileName().toString());

        try {
            InputStream in = new FileInputStream(copy_form.toFile());
            Files.copy(in, copy_to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copying from an InputStream to a File
     *The input stream may be extracted in other ways.
     * For example, the following code snippet will get the input stream from an Internet URL
     * (It will copy the picture indicated by the URL to the E:/tmp/photo directory only if the the file doex not exist)
     */
    @Test
    public void copyingFromInputStream() {
        Path copy_to = Paths.get("E:/tmp/photo/123.jpg");
        URI u = URI.create("http://h.hiphotos.baidu.com/image/h%3D200/sign=baea7d793d01213fd03349dc64e636f8/4034970a304e251fa230ad58a386c9177e3e534e.jpg");
        try(InputStream in = u.toURL().openStream()) {
            Files.copy(in, copy_to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copying from a File to an OutputStream
     */
    @Test
    public void copyingFromAFileToAnOutputStream() {
        Path copy_from = Paths.get("E:/idea/nio/456.txt");
        Path copy_to = Paths.get("E:/tmp/456.txt");

        try(OutputStream out = new FileOutputStream(copy_to.toFile())) {
            Files.copy(copy_from, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Moving Files and Directories
    StandardCopyOption
        -- REPLACE_EXISTING
            If the target file already exists, then the move is still performed and the target is replaced.
            When dealing with a symbolic link, the symbolic link is replaced but it point to is not affected.
        -- ATOMIC_MOVE
            The file move will be performed as an atomic operation, which guarantees that any process that monitors the file's directory will access a complete file.
    */
    @Test
    public void movingFilesAndDirectories() {
        Path movefrom = FileSystems.getDefault().getPath("E:/tmp/123.txt");
        Path moveto = FileSystems.getDefault().getPath("E:/123.txt");
        try {
            Files.move(movefrom, moveto, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Path movefrom = FileSystems.getDefault().getPath("C:/rafaelnadal/rafa_2.jpg");
        Path moveto_dir = FileSystems.getDefault().getPath("C:/rafaelnadal/photos");
        try {
            Files.move(movefrom, moveto_dir.resolve(movefrom.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
        */
    }

    /**
     * Rename a File
     */
    @Test
    public void renameAFile() {
        Path movefrom = FileSystems.getDefault().getPath("E:/tmp/123.txt");

        try {
            Files.move(movefrom, movefrom.resolveSibling("hello.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}

