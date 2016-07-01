package com.rookieInn.nio.recursiveOperations;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by gxy on 2016/6/16.
 */
public class SearchFileByContent {
    public static void main(String[] args) {
        String words = "Rafael Nadal,tennis,winner of Roland Garros,BNP Paribas tournament draws";
        ArrayList<String> wordsarray = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(words, ",");
        while (st.hasMoreTokens()) {
            wordsarray.add(st.nextToken());
        }
        System.out.println(searchTest(wordsarray, "tenniss"));
    }

    private static boolean searchTest(ArrayList<String> wordsarray, String text) {
        boolean flag = false;
        for (int j = 0; j < wordsarray.size(); j++) {
            if ((text.toUpperCase()).contains(wordsarray.get(j).toUpperCase())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
