package com.rookieInn.nio;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 把E:/tmp/1.zip 中的1.jpg 复制到 E:/tmp/hehheda.jpg
 * Created by gxy on 2016/7/1.
 */
public class WorkingWithZipFileSystemProvider {

    public static void main(String[] args) {
        //set zip file system properties
        Map<String, String> env = new HashMap<>();
        env.put("create", "false");
        env.put("encoding", "ISO-8859-1");

        //locate file system with java.net.JarUrlConnection
        URI uri = URI.create("jar:file:/E:/tmp/1.zip");

        try(FileSystem ZipFS = FileSystems.newFileSystem(uri, env)) {
            Path fileInZip = ZipFS.getPath("1.jpg");
            Path fileOutZip = Paths.get("E:/tmp/hehheda.jpg");

            //copy  outside the archive
            Files.copy(fileInZip, fileOutZip);

            System.out.println("The file was successfully copied");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
