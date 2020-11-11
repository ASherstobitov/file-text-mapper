package com.fileread.repository;

import com.fileread.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,String> {
    FileEntity findById(Long id);
}
