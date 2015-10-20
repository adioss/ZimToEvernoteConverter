/**
 * Copyright 2015 Adrien PAILHES
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adioss.zimevernote;

import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Converter {
    private final FileHelper fileHelper;
    private static final String TABLE_BEGIN = "<table style=\"-evernote-table:true;border-collapse:collapse;table-layout:fixed;margin-left:0px;width:100%;\"><tr><td style=\"border-style:solid;border-width:1px;border-color:rgb(211,211,211);padding:10px;margin:0px;width:99.92800575953925%;\">";
    private static final String UN_ORDERED_LIST_TAG_BEGIN = "<ul>";
    private static final String UN_ORDERED_LIST_TAG_END = "</ul>";
    private static final String LIST_ITEM_TAG_BEGIN = "<li>";
    private static final String LIST_ITEM_LIST_TAG_END = "</li>";
    private static final String TITLE_PREFIX = "===== ";
    private static final String TITLE_SUFFIX = " =====";
    private static final String ZIM_TABLE_BEGIN = "\\{\\{\\{code: lang=\"java\" linenumbers=\"True\"";

    public Converter() {
        fileHelper = new FileHelper();
    }


    public Content apply(String inputFileAsString) throws IOException {

        String previousLineConverted = null;
        boolean isTableReplacementStarted = false;
        List<String> lines = removeHeaderLines(inputFileAsString.split("\n"));
        String fileName = extractFileName(lines.get(0));
        if (fileName == null) {
            return null;
        }
        String content = createHeader(fileName);
        lines = lines.subList(1, lines.size());
        for (String line : lines) {
            if (line.startsWith("*") || line.startsWith("-")) {
                line = addUnOrderedListItem(line, previousLineConverted);
            } else if (previousLineConverted != null && previousLineConverted.endsWith(LIST_ITEM_LIST_TAG_END)) {
                line = UN_ORDERED_LIST_TAG_END + line;
            }
            if (line.contains("{{{")) {
                line = replaceByTable(line);
                isTableReplacementStarted = true;
            } else if (line.startsWith("}}}")) {
                line = "</td></tr></table>";
                isTableReplacementStarted = false;
            } else if (isTableReplacementStarted) {
                line = replaceByLine(line);
            }
            content += line;
            previousLineConverted = line;
        }
        content += createFooter();
        return new Content(fileName, content);
    }

    private String createFooter() {
        return "</en-note>]]></content><created>20151016T155014Z</created><updated>20151016T163142Z</updated>" +
                "<note-attributes><author>adrien pailhes</author><source-url>about:blank</source-url></note-attributes>" +
                "</note></en-export>";
    }

    private String replaceByLine(String line) {
        line = replaceSpaces(line);
        line = "<div>" + line + "</div>";
        return line;
    }

    private String replaceByTable(String line) {
        return line.replaceAll(ZIM_TABLE_BEGIN, TABLE_BEGIN);
    }

    private String extractFileName(String firstLine) {
        try {
            firstLine = firstLine.substring(firstLine.indexOf(TITLE_PREFIX) + TITLE_PREFIX.length(), firstLine.length());
            firstLine = firstLine.substring(0, firstLine.indexOf(TITLE_SUFFIX));
        } catch (Exception e) {
            return null;
        }
        return firstLine;
    }

    private List<String> removeHeaderLines(String[] split) {
        List<String> splitAsList = Arrays.asList(split);
        if (splitAsList.size() < 4) {
            return splitAsList;
        }
        return splitAsList.subList(4, split.length);
    }

    private String addUnOrderedListItem(String line, String previousLine) {
        String result = "";
        if (previousLine == null || !previousLine.startsWith(UN_ORDERED_LIST_TAG_BEGIN)) {
            result += UN_ORDERED_LIST_TAG_BEGIN;
        }
        line = line.substring(1, line.length());
        result += "<li style=\"background-color: rgb(255, 255, 255);\">" + replaceSpaces(line) + "</li>";
        return result;
    }

    private String replaceSpaces(String line) {
        return HtmlEscapers.htmlEscaper().escape(line).replaceAll(" ", "&nbsp;");
    }

    private String createHeader(String fileName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export2.dtd\">\n" +
                "<en-export export-date=\"20151016T163150Z\" application=\"Evernote/Windows\" version=\"5.x\">\n" +
                "<note><title>" + fileName + "</title><content><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\"> \n <en-note>";
    }


}
