package com.kindnesskattle.bddAtcProject.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class S3UploadService {

    private final AmazonS3 s3Client;

    public S3UploadService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    public void uploadPhoto(String bucketName, String keyName, String filePath) {
        File file = new File(filePath);
        s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
    }
}
