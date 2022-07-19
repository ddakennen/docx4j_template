package com.kennen.template4j;

import java.io.IOException;
import org.docx4j.openpackaging.exceptions.Docx4JException;

public class Application {

    public static void main(String[] args) throws Docx4JException, IOException {
        final String saveFilePath = System.getProperty("user.dir") + "/build/welcome.docx";
        DocxTemplate docxTemplate = new DocxTemplate(saveFilePath);
        // add title
        docxTemplate.addTitleParagraph("Hello World!");
        // add normal paragraph
        docxTemplate.addParagraph("Welcome To Docx4j World");
        // add highlight paragraph
        docxTemplate.addHighlightParagraph("Highlight");
        docxTemplate.save();
        docxTemplate.savePdf();
    }
}
