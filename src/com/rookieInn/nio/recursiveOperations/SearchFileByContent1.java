package com.rookieInn.nio.recursiveOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by gxy on 2016/6/16.
 */
public class SearchFileByContent1 implements FileVisitor {

    ArrayList<String> wordsarray = new ArrayList<>();
    ArrayList<String> documents = new ArrayList<>();
    boolean found = false;

    public SearchFileByContent1(String words) {
        wordsarray.clear();
        documents.clear();

        StringTokenizer st = new StringTokenizer(words, ",");
        while (st.hasMoreTokens()) {
            wordsarray.add(st.nextToken().trim());
        }
    }

    public void search(Path file) throws IOException {
        found = false;

        String name = file.getFileName().toString();
        int mid = name.lastIndexOf(",");
        String ext = name.substring(mid + 1, name.length());
        if ((ext.equalsIgnoreCase("txt")) || (ext.equalsIgnoreCase("xml")
                || ext.equalsIgnoreCase("html"))
                || ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("xhtml")
                || ext.equalsIgnoreCase("rtf")) {
            searchInText(file);
        }

        if (found) {
            documents.add(file.toString());
        }
    }

    //search in text files
    public boolean searchInText(Path file) {
        boolean flag = false;
        Charset charset = Charset.forName("UTF-8");
        try(BufferedReader reader = Files.newBufferedReader(file, charset)){
            String line = null;

            OUTERMOST:
            while((line = reader.readLine()) != null){
                flag = searchText(line);
                if (flag) {
                    break OUTERMOST;
                }
            }
        } catch (IOException e) {

        } finally {
            return flag;
        }
    }

    //search text
    private boolean searchText(String text) {
        boolean flag = false;
        for (int j = 0; j < wordsarray.size(); j++) {
            if((text.toLowerCase()).contains(wordsarray.get(j).toLowerCase())) {
                flag =true;
                break;
            }
        }
        return flag;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        search((Path) file);
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
}
