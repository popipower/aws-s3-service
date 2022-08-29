package com.checkpro.extractor.controller;

import com.amazonaws.regions.Regions;
import com.checkpro.extractor.model.BucketData;
import com.checkpro.extractor.model.ObjectData;
import com.checkpro.extractor.service.BucketStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("/api")
public class BucketStorageController {

    @Autowired
    private BucketStorageService bucketStorageService;

    @PostMapping(value = "/upload",produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectData handleFileUpload(@RequestParam(value = "file", required = true) MultipartFile file,
                                       @RequestHeader(value = "userId", required = true) String userId,
                                       @RequestHeader(value = "transactionId", required = true) String transactionId ) {
        return bucketStorageService.saveObject(file,userId,transactionId);
    }

    @PostMapping(value = "/bucket",produces = MediaType.APPLICATION_JSON_VALUE)
    public BucketData bucket(@RequestHeader(value = "bucketName", required = false, defaultValue = "check-pro-transactions")
                                         String bucketName) {
        return bucketStorageService.createBucket(bucketName);
    }
}

