package com.github.dearmann.matchservice.controller;

import com.github.dearmann.matchservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final FileService fileService;

    @GetMapping("/{bucketName}")
    public List<String> listObjectsUrls(@PathVariable String bucketName) {
        return fileService.listObjectsUrls(bucketName);
    }

    @GetMapping("/{bucketName}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadObject(@PathVariable String fileName,
                                                              @PathVariable String bucketName) {
        return fileService.downloadObject(fileName, bucketName);
    }

    @PostMapping("/{bucketName}")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadObject(@RequestParam("file") MultipartFile file,
                               @PathVariable String bucketName) {
        return fileService.uploadObject(file, bucketName);
    }

}

