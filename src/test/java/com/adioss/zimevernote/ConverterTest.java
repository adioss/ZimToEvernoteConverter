package com.adioss.zimevernote;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.adioss.zimevernote.FileHelper.fileToString;

public class ConverterTest {
    private static final String ZIM_FILE_NAME = "zim.txt";
    private static final String EVERNOTE_FILE_NAME = "evernote.txt";
    private static final String TEMP_FILE_NAME = "/outputFile.txt";
    // key size must be 16 or 128 for AES
    private static final String KEY = "Mary has one cat";
    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_TRANSFORMATION_ALGORITHM = "AES";

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testConvert() throws Exception {
        // File outputFile = testFolder.newFile(TEMP_FILE_NAME);
        // Given
        File zimFile = loadFile(ZIM_FILE_NAME);
        File evernoteFile = loadFile(EVERNOTE_FILE_NAME);
        String zimFileToString = fileToString(zimFile);
        String evernoteFileToString = fileToString(evernoteFile);

        // When
        Content result = new Converter().apply(fileToString(zimFile));


        // Then
        Assert.assertEquals(result.getFileName(), "Affichage correcte de la log");
        String content = result.getContent();
//        Assert.assertEquals(content, evernoteFileToString);
    }

    private File loadFile(String fileName) throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getResource(fileName);
        return new File(resourceUrl.toURI());
    }
}