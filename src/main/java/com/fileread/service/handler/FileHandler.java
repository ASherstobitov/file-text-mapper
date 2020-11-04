package com.fileread.service.handler;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

@Component
public class FileHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);
    private static final String OUTPUT = "test.docx";

    private NavigableMap<String, String> content = new TreeMap<>();

    public NavigableMap<String, String> getTOCFromWordFile(MultipartFile multipartFile) {
        try {
            InputStream initStream = multipartFile.getInputStream();
            XWPFDocument document = new XWPFDocument(initStream);
            List<XWPFParagraph> paragraphList = document.getParagraphs();
            paragraphList.stream()
                    .map(XWPFParagraph::getParagraphText)
                    .filter(line -> line.contains("#"))
                    .forEach(this::handleLine);
            content.forEach((key, value) -> System.out.println(key + " " + value));
        } catch (IOException e) {
            LOGGER.error("Error during read of uploaded word file");
        }
        return content;
    }

    public NavigableMap<String, String> getTOCFromFile(MultipartFile multipartFile) {
        try {
            InputStream initStream = multipartFile.getInputStream();
            new BufferedReader(new InputStreamReader(initStream, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> line.contains("#"))
                    .forEach(this::handleLine);
            content.forEach((key, value) -> System.out.println(key + "  " + value));
        } catch (IOException e) {
            LOGGER.error("Error during read of uploaded file");
        }
        return content;
    }

    private void handleLine(String line) {
        int count = (int) line.chars().filter(ch -> ch == '#').count();
        String filteredLine = line.replace("#", "");

        if (count == 1) {
            if (content.firstEntry() == null) {
                content.put(String.valueOf(1), filteredLine);
            } else {
                int num = Integer.parseInt(content.lastKey().substring(0, 1));
                content.put(String.valueOf(++num), filteredLine);
            }
        } else {
            String lastKey = content.lastKey().replace(".", "");
            int lastKeyLength = lastKey.length();
            if (lastKeyLength == count) {
                String subtitleNum = lastKey.substring(0, lastKeyLength - 1);
                int subtitleNewNum = Integer.parseInt(lastKey.substring(lastKeyLength - 1));
                String newNum = subtitleNum + ++subtitleNewNum;
                String num = newNum.replaceAll("\\B", ".");
                content.put(num, filteredLine);
            }
            if (lastKeyLength < count) {
                String num = (lastKey + 1).replaceAll("\\B", ".");
                content.put(num, filteredLine);
            }
            if (lastKeyLength > count) {
                String sub = lastKey.substring(0, count);
                String newStr = sub.substring(0, --count) + (Integer.parseInt(sub.substring(sub.length() - 1)) + 1);
                newStr = newStr.replaceAll("\\B", ".");
                content.put(newStr, filteredLine);
            }
        }
    }

    public void createWordDoc() {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(OUTPUT)) {
            createParagraph(document);
            document.write(out);
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format("File with name %s isn't found", OUTPUT));
        } catch (IOException e) {
            LOGGER.error("Error during close the stream");
        }
    }

    private void createParagraph(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        CTP ctP = paragraph.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff.TRUE);
    }
}
