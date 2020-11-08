package com.fileread.service;

import com.fileread.model.FileDB;
import com.fileread.repository.FileDBRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Data
public class DBFileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;



    public void store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        fileDBRepository.save(fileDB);
    }


}
