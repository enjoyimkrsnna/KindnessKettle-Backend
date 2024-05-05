package com.kindnesskattle.bddAtcProject.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class S3UploadService {

    private final AmazonS3 s3Client;

    public S3UploadService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    public void uploadPhotoToProfiles(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String keyName = "Profiles/" + fileName;
        uploadPhoto("unique-kindnesskettle-image", keyName, file);
    }


    public void uploadPhotoToFoodPost( MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String keyName = "FoodPost/" + fileName;
        uploadPhoto("unique-kindnesskettle-image", keyName, file);
    }

    public File downloadFileFromProfiles(String fileName) {
        String keyName = "Profiles/" + fileName;
        return downloadFile("unique-kindnesskettle-image", keyName);
    }

    public File downloadFileFromFoodPost(String fileName) {
        String keyName = "FoodPost/" + fileName;
        return downloadFile("unique-kindnesskettle-image", keyName);
    }

    private void uploadPhoto(String bucketName, String keyName, MultipartFile file) {
        try {
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), new ObjectMetadata()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File downloadFile(String bucketName, String keyName) {
        File tempFile = null;
        try {
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
            InputStream inputStream = object.getObjectContent();
            tempFile = File.createTempFile("temp", null);
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException | AmazonS3Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }

}
