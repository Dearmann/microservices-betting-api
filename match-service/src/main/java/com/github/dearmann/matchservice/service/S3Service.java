package com.github.dearmann.matchservice.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    private final AmazonS3 s3Client;

    @Override
    public List<String> listObjectsUrls(String bucketName) {
        List<String> objectsUrls = new ArrayList<>();
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            String url = s3Client.getUrl(bucketName, objectSummary.getKey()).toString();
            objectsUrls.add(url);
        }
        return objectsUrls;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadObject(String fileName, String bucketName) {
        try {
            S3Object object = s3Client.getObject(bucketName, fileName);
            S3ObjectInputStream objectInputStream = object.getObjectContent();
            InputStreamResource inputStreamResource = new InputStreamResource(objectInputStream);
            return ResponseEntity.ok()
                    .contentLength(object.getObjectMetadata().getContentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadObject(MultipartFile file, String bucketName) {
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + filenameExtension;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        s3Client.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
        return s3Client.getUrl(bucketName, fileName).toString();
    }

}

