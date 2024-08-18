package com.shakir.scholarForge.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
@Configuration
public class StorageConfig {
	@Value("${aws.access-key}")
	private String accessKey;
	@Value("${aws.secret-key}")
	private String secretKey;
	@Value("${s3.region.name}")
	private String region;
	
	@Bean
	public AmazonS3 generateS3Client() {
		AWSCredentials credentials=new BasicAWSCredentials(accessKey,secretKey);
		return AmazonS3ClientBuilder.standard().
				withCredentials(new AWSStaticCredentialsProvider(credentials))
		.withRegion(region).build();
		
	}

}
