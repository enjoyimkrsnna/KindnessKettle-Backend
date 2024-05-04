package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kindnessKettle")
@Slf4j
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @Autowired
    public S3UploadController(S3UploadService s3UploadService) {
        this.s3UploadService = s3UploadService;
    }


    @PostMapping("/uploadPhoto")
    public String uploadPhoto(@RequestParam String bucketName, @RequestParam String keyName, @RequestPart("file") MultipartFile file) {
        log.info("Received request to upload photo. BucketName: {}, KeyName: {}, File Name: {}, Content Type: {}, File Size: {} bytes",
                bucketName, keyName, file.getOriginalFilename(), file.getContentType(), file.getSize());

        s3UploadService.uploadPhoto(bucketName, keyName, file);
        return "Photo uploaded successfully.";
    }
}
