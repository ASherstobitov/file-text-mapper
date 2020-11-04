package com.fileread.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class FileTransfer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTransfer.class);
    private static final String TARGET_FILE_PATH_1 = "C:\\java-solvery\\test1.txt";


    public void transferFile(MultipartFile multipartFile) {
        File file = new File(TARGET_FILE_PATH_1);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            LOGGER.error("Can't transfer file to specified place");
        }
    }

    public void writeUsingBytes(MultipartFile multipartFile) {
        File file = new File(TARGET_FILE_PATH_1);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOGGER.error("Can't write to target file");
        }
    }

    public void writeToFile(MultipartFile multipartFile) {
        try {
            InputStream initStream = multipartFile.getInputStream();
            byte[] buffer = new byte[initStream.available()];
            initStream.read(buffer);

            File targetFile = new File(TARGET_FILE_PATH_1);

            try (OutputStream outputStream = new FileOutputStream(targetFile)) {
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            LOGGER.error("Exception in uploaded file");
        }
    }
}
