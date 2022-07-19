package com.kennen.template4j;

import com.kennen.template4j.enums.FileTypes;
import com.kennen.template4j.enums.LiveStyles;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Color;
import org.docx4j.wml.Highlight;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;

public class DocxTemplate {
    private final WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
    private final MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
    private final File exportFile;
    private JcEnumeration titleEnumeration = JcEnumeration.CENTER;
    private String title;
    private String fileName;
    private String directory;
    public DocxTemplate(String saveFilePath) throws InvalidFormatException {
        exportFile = new File(saveFilePath);
        fileName = exportFile.getAbsoluteFile().getName().replaceAll("\\.\\w*", "");
        directory = exportFile.getAbsoluteFile().getParent();
        title = fileName;
    }

    public void save() throws Docx4JException {
        wordPackage.save(exportFile);
    }

    public void addTitleParagraph(String text) {
        final ObjectFactory factory = Context.getWmlObjectFactory();
        final PPr pPr = getpPr(factory, titleEnumeration);
        final P p = getP(text);
        p.setPPr(pPr);
        mainDocumentPart.getContent().add(p);
    }

    public void addParagraph(String text) {
        mainDocumentPart.addParagraphOfText(text);
    }

    public void addHighlightParagraph(String text){
        final ObjectFactory factory = Context.getWmlObjectFactory();
        final Text factoryText = getText(text, factory);
        P p = factory.createP();
        R r = factory.createR();
        r.getContent().add(factoryText);

        RPr rpr = getHighlightRPr(factory, "green", "000000");
        r.setRPr(rpr);
        p.getContent().add(r);
        mainDocumentPart.getContent().add(p);
    }

    private P getP(String text){
        final ObjectFactory factory = Context.getWmlObjectFactory();
        final Text factoryText = getText(text, factory);
        P p = factory.createP();
        R r = factory.createR();
        r.getContent().add(factoryText);
        p.getContent().add(r);
        return p;
    }

    private PPr getpPr(ObjectFactory factory, JcEnumeration enumeration) {
        Jc justification = factory.createJc();
        justification.setVal(enumeration);
        PPr paragraphProperties = factory.createPPr();
        paragraphProperties.setJc(justification);
        return paragraphProperties;
    }

    private RPr getHighlightRPr(ObjectFactory factory, String highlightValue, String fontColorValue) {
        final Color color = getColor(factory, fontColorValue);
        final Highlight highlight = getHighlight(factory, highlightValue);
        RPr rpr = factory.createRPr();
        rpr.setColor(color);
        rpr.setHighlight(highlight);
        return rpr;
    }

    private Highlight getHighlight(ObjectFactory factory, String highlightValue) {
        final Highlight highlight = factory.createHighlight();
        highlight.setVal(highlightValue);
        return highlight;
    }

    private Color getColor(ObjectFactory factory, String value) {
        Color fontColor = factory.createColor();
        fontColor.setVal(value);
        return fontColor;
    }

    private Text getText(String text, ObjectFactory factory) {
        final Text factoryText = factory.createText();
        factoryText.setValue(text);
        return factoryText;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void savePdf() throws IOException {
        File pdfOutFile = new File(getSaveFileName(FileTypes.PDF));
        InputStream doc = new FileInputStream(exportFile);
        XWPFDocument document = new XWPFDocument(doc);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(pdfOutFile);
        PdfConverter.getInstance().convert(document, out, options);

        PDDocument pdDocument = PDDocument.load(pdfOutFile);
        PDDocumentInformation pdd = pdDocument.getDocumentInformation();
        pdd.setTitle(title);
        pdDocument.save(pdfOutFile);
        pdDocument.close();
        doc.close();
        out.close();
    }

    private String getSaveFileName(FileTypes fileType) {
        return directory.concat("/").concat(fileName).concat(fileType.getValue());
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
