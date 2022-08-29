package com.checkpro.extractor.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.checkpro.extractor.model.BucketData;
import com.checkpro.extractor.model.ObjectData;
import com.checkpro.extractor.util.ExtractorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
public class BucketStorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BucketStorageService.class);

    @Value("${aws.s3.bucket.name.key}")
    private String bucketNameKey;

    @Value("${aws.s3.signed.url.expiry.in.minute.key}")
    private String expiryKey;

    @Value("${aws.s3.region.name}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private ExtractorUtil extractorUtil;

    public ObjectData saveObject(MultipartFile file,String userId, String transactionId) {
        ObjectData objectData = new ObjectData();
        String fileName = file.getOriginalFilename();
        String bucketName = extractorUtil.getParameterValue(bucketNameKey);
        String keyName = new StringBuilder().append(userId).append("/").append(transactionId).append("/")
                .append(fileName).toString();
        LOGGER.info("Saving the key {} in bucket {} for region {}",keyName,bucketName,region);
        PutObjectResult putObjectResult = uploadObject(bucketName,fileName, keyName,file);
        if(putObjectResult!=null){

            String signedURL= generateSignedURL(bucketName,keyName);
            objectData = new ObjectData(signedURL, fileName, transactionId,userId);
        }
        return objectData;
    }

    private String generateSignedURL(String bucket, String objectKey) {
        URL url = null;
        try {
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis += 1000 * 60 * Integer.valueOf(extractorUtil.getParameterValue(expiryKey)) ;
            expiration.setTime(expTimeMillis);

            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            LOGGER.info("Pre-Signed URL: {}" , url.toString());
        } catch (SdkClientException e) {
            LOGGER.error("Exception occurred while generating PreSigned URl for key {} in bucket {} with trace",objectKey, bucket,e);
        }
        return url!=null? url.toString(): "";
    }


    private PutObjectResult uploadObject(String bucketName,String fileName, String keyName,MultipartFile file) {

        PutObjectResult putObjectResult = null;
        LOGGER.info("Uploading {} to S3 bucket {}", fileName, bucketName);
        try {
            S3Object s3Object = new S3Object();

            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentType(file.getContentType());
            omd.setContentLength(file.getSize());
            omd.setHeader("filename", fileName);

            ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());

            s3Object.setObjectContent(bis);
            putObjectResult = s3Client.putObject(new PutObjectRequest(bucketName, keyName, bis, omd));
            s3Object.close();
            //s3Client.putObject(bucketName, keyName, new File(filePath));
        } catch (AmazonServiceException | IOException e) {
            LOGGER.error("Exception occurred while uploading file {} in bucket {} with trace ",keyName, bucketName,e);
            e.printStackTrace();
        }

        return putObjectResult;
    }


    public BucketData createBucket(String bucketName) {
        BucketData bucketData = new BucketData(bucketName);
        Bucket bucket = null;
        if (s3Client.doesBucketExistV2(bucketName)) {
            LOGGER.warn("Bucket {} already exists", bucketName);
            bucket = getBucket(bucketName);
            bucketData.setAlreadyExist(true);
        } else {
            try {
                bucket = s3Client.createBucket(bucketName);
            } catch (AmazonS3Exception e) {
                LOGGER.error("Exception while creating bucket {}{}",bucketName,e.getErrorMessage());
            }
        }
        if (bucket == null) {
            bucketData.setError(true);
            LOGGER.error("Error while creating bucket {}",bucketName);
        } else if(!bucketData.getAlreadyExist()) {
            bucketData.setCreated(true);
            LOGGER.info("Bucket {} successfully created",bucketName);
        }

        return bucketData;
    }

    private Bucket getBucket(String bucketName) {
        Bucket namedBucket = null;
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucketName)) {
                namedBucket = b;
                break;
            }
        }
        return namedBucket;
    }




}
