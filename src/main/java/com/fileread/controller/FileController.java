package com.fileread.controller;

import com.fileread.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NavigableMap;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/uploadFile")
    public NavigableMap<String, String> getTOCFromFIle(@RequestParam("file") MultipartFile file) {
        return fileStorageService.getTOCFromFIle(file);
    }

    @GetMapping("/number")
    public String getPhoneNumber(@RequestParam(name = "id", required = false) String id) {
        return id;
    }
}
