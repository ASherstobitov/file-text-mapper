package com.fileread.repository;

import com.fileread.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileDBRepository extends JpaRepository<FileDB,String> {

}
