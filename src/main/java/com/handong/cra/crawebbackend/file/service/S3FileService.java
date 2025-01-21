package com.handong.cra.crawebbackend.file.service;

import com.handong.cra.crawebbackend.file.domain.S3ImageCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3FileService {

    public String uploadFile(MultipartFile file, S3ImageCategory s3ImageCategory);
}
