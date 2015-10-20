package com.adioss.zimevernote;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    public FileHelper() {
    }

    public static String fileToString(File outputFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(outputFile.getPath())));
    }

    public static void stringToFile(Path outputPath, String content) throws IOException {
        Files.write(outputPath, content.getBytes());
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    File createFile(String fileName, Path root) {
        return new File(root.toString() + "\\" + fileName);
    }

    File createTxtFile(String fileName, Path root) {
        return new File(root.toString() + "\\" + fileName + ".txt");
    }

    Path createDirectory(Path dir) throws IOException {
        deleteDirectoryRecursively(dir.toFile());
        return Files.createDirectory(dir);
    }

    private void deleteDirectoryRecursively(File file) {
        File[] subFiles = file.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                deleteDirectoryRecursively(subFile);
            }
        }
        file.delete();
    }
}
