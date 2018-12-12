package org.finace.utils.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Ness on 2017/1/2.
 */
public class ReadBigFileByLine {
    public static void main(String[] args) throws IOException {
        InputStream is = ReadBigFileByLine.class.getClassLoader().getResourceAsStream("1.properties");
        Scanner sacnner = new Scanner(is, "utf-8");
        while(sacnner.hasNextLine()){
            System.out.println(sacnner.nextLine());
        }

    }
}
