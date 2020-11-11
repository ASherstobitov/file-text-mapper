package com.fileread.controller;

import com.fileread.service.FileStorageService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.NavigableMap;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

//    get the file
    @PostMapping("/toc")
    public NavigableMap<String, String> getTOCFromFIle(@RequestParam(name = "file") MultipartFile file) {
        return fileStorageService.getTOCFromFIle(file);
    }

    @PostMapping
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        fileStorageService.uploadFile(file);
    }

    @GetMapping("/{id}")
    public Resource downloadFile(@PathVariable(name = "id") Long id) {
        return fileStorageService.downloadFile(id);
    }
}
