package com.fileread.service.impl;

import com.fileread.configuration.FileStorageProperties;
import com.fileread.model.FileEntity;
import com.fileread.repository.FileRepository;
import com.fileread.service.FileStorageService;
import com.fileread.service.handler.FileHandler;
import com.fileread.service.handler.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NavigableMap;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path fileStorageLocation;
    private final FileTransfer fileTransfer;
    private final FileHandler fileHandler;
    private final FileRepository fileRepository;

    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties,
                                  FileTransfer fileTransfer, FileHandler fileHandler, FileRepository fileRepository) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileTransfer = fileTransfer;
        this.fileHandler = fileHandler;
        this.fileRepository = fileRepository;
    }

    public NavigableMap<String, String> getTOCFromFIle(MultipartFile multipartFile) {

//        try {
//            dbFileStorageService.store(multipartFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

//        LOGGER.info("from word file");
//        NavigableMap<String, String> map = fileHandler.getTOCFromWordFile(multipartFile);

        LOGGER.info("from txt file");
        NavigableMap<String, String> map = fileHandler.getTOCFromFile(multipartFile);
        fileTransfer.transferFile(multipartFile);
        fileHandler.createWordDoc();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            LOGGER.error(String.format("Can't store file with name %s to target location", fileName));
        }
        return map;
    }

    @Override
    public void uploadFile(MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileEntity file = null;
        try {
            file = new FileEntity(fileName, multipartFile.getContentType(), multipartFile.getBytes());
        } catch (IOException e) {
           LOGGER.error("Can't create file from multipart file");
        }
        fileRepository.save(file);
    }

    public Resource downloadFile(Long id) {
        FileEntity fileEntity = fileRepository.findById(id);
        return new ByteArrayResource(fileEntity.getData());
    }
}
