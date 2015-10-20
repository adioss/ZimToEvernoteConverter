package com.adioss.zimevernote;

import java.util.List;

public class Content {
    private final String fileName;
    private final String content;

    public Content(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }
}
