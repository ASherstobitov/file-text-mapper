package com.fileread.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.NavigableMap;

public interface FileStorageService {
    NavigableMap<String, String> getTOCFromFIle(MultipartFile file);
}
