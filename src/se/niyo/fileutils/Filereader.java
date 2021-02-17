package se.niyo.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Filereader {

    public static byte[] readFromFile(File file) {
        byte[] content = null;
        System.out.println("Does the file exist: " + file.exists());
        if (file.exists() && file.canRead()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                content = new byte[(int) file.length()];
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return content;
    }
}
