package com.adioss.zimevernote;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.adioss.zimevernote.FileHelper.deleteDirectory;
import static com.adioss.zimevernote.FileHelper.stringToFile;
import static java.nio.file.Files.createDirectory;

public class ZimToEvernoteConverter {

    private Path inputFolder = null;
    private Path outputFolder = null;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        ZimToEvernoteConverter converter = new ZimToEvernoteConverter();
        converter.parseParams(args);

        deleteDirectory(new File(String.valueOf(converter.getOutputFolder())));
        createDirectory(converter.getOutputFolder());

        Path inputFolder = converter.getInputFolder();
        extractFolder(converter, inputFolder, converter.getOutputFolder());


    }

    private static void extractFolder(ZimToEvernoteConverter converter, Path inputFolder, Path outputFolder) throws IOException {
        Path tempOutputFolder = Paths.get(outputFolder + "\\" + inputFolder.getFileName());
        System.out.println("Creating " + tempOutputFolder.toString());
        createDirectory(tempOutputFolder);
        Files.list(inputFolder).forEach(path -> {
            if (Files.isDirectory(path)) {
                try {
                    extractFolder(converter, path, tempOutputFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                extractToPath(converter, path, tempOutputFolder);
            }

        });
    }

    private static void extractToPath(ZimToEvernoteConverter converter, Path path, Path outputFolder) {
        try {
            Content content = new Converter().apply(FileHelper.fileToString(path.toFile()));
            if (content == null) {
                return;
            }
            String outputFilePath = outputFolder + "\\" + content.getFileName() + ".enex";
            System.out.println("Write file " + outputFilePath);
            stringToFile(Paths.get(outputFilePath), content.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseParams(String[] args) {
        if (args.length == 0) {
            System.out.println("ZimToEvernoteConverter [PARAMETERS]");
            System.out.println("  -inputFolder {the input folder with zim formatted files}");
            System.out.println("  -outputFolder {the output folder that will contains generated .enex files}");

            System.out.println("\n");
            System.out.println("Example:");
            System.out.println("ZimToEvernoteConverter - -inputFolder \"c:\\input\" -outputFolder \"c:\\output\" \n\n");

            System.exit(1);
        }

        String inputPathArgument = null;
        String outputFolderArgument = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-inputFolder")) {
                inputPathArgument = args[i + 1];
            } else if (arg.equals("-outputFolder")) {
                outputFolderArgument = args[i + 1];
            }
        }
        if (isNullOrEmpty(inputPathArgument)) {
            exitWithError("InputFile was not specified.");
        } else if (!Files.exists(Paths.get(inputPathArgument))) {
            exitWithError("InputFile is not correct.");
        }

        if (isNullOrEmpty(outputFolderArgument)) {
            exitWithError("OutputFolder was not specified.");
        }

        inputFolder = Paths.get(inputPathArgument);
        outputFolder = Paths.get(outputFolderArgument);
    }

    private boolean isNullOrEmpty(String inputFile) {
        return inputFile == null || "".equals(inputFile);
    }

    private void exitWithError(String message) {
        System.out.println(message);
        System.exit(1);
    }

    public Path getInputFolder() {
        return inputFolder;
    }

    public Path getOutputFolder() {
        return outputFolder;
    }
}
