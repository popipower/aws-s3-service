package com.checkpro.extractor.configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractorConfiguration {

    @Value("${aws.s3.region.name}")
    private String region;

    @Bean
    public AmazonS3 s3Client(){
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region)).build();
    }

    @Bean
    public AWSSimpleSystemsManagement ssmClient(){
        return AWSSimpleSystemsManagementClientBuilder
                .standard().withRegion(region).build();
    }
}
