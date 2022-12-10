package com.github.dearmann.matchservice.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<String> listObjectsUrls(String bucketName);

    ResponseEntity<InputStreamResource> downloadObject(String fileName, String bucketName);

    String uploadObject(MultipartFile file, String bucketName);
}
