package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String uploadPhoto(@RequestParam String bucketName, @RequestParam String keyName, @RequestParam String filePath) {
        s3UploadService.uploadPhoto(bucketName, keyName, filePath);
        return "Photo uploaded successfully.";
    }
}
