package com.kindnesskattle.bddAtcProject.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class S3UploadService {

    private final AmazonS3 s3Client;

    public S3UploadService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    public void uploadPhoto(String bucketName, String keyName, MultipartFile file) {
        try {
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), new ObjectMetadata()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
