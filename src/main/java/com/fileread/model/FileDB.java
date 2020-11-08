package com.fileread.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NonNull
    private String fileName;

    @NonNull
    private String fileType;

    @NonNull
    @Lob
    private byte[] data;
}

