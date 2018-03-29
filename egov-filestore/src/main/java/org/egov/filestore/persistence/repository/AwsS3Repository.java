package org.egov.filestore.persistence.repository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.egov.filestore.domain.model.FileLocation;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
/**
 * 
 * @author kaviyarasan1993
 *
 */
public class AwsS3Repository {
	
	@Value("${aws.key}")
	private String key;
	
	@Value("${aws.secretkey}")
	private String secretKey;
	
	@Value("${aws.region}")
	private String awsRegion;
	
	private static final String TEMP_FILE_PATH_NAME = "TempFolder/localFile";

	public void writeToS3(MultipartFile file, FileLocation fileLocation) {

		AmazonS3 s3Client = getS3Client();
		
		String completeName = fileLocation.getFileName();
		
		int index = completeName.indexOf('/');
		String bucketName = completeName.substring(0,index);
		String fileNameWithPath = completeName.substring(index+1,completeName.length());

		if (!s3Client.doesBucketExistV2(bucketName))
			s3Client.createBucket(bucketName);

		InputStream is = null;
		long contentLength = file.getSize();

		try {
			is = file.getInputStream();
		} catch (IOException e) {
			log.error(" exception occured while reading input stream from file ", e);
			throw new RuntimeException(e);
		}

		ObjectMetadata objMd = new ObjectMetadata();
		objMd.setContentLength(contentLength);
		
		s3Client.putObject(bucketName, fileNameWithPath, is, objMd);

	}
	
	public Resource getObject(String completeName) {

		long startTime = new Date().getTime();
		
		AmazonS3 s3Client = getS3Client();
		
		int index = completeName.indexOf('/');
		String bucketName = completeName.substring(0,index);
		String fileNameWithPath = completeName.substring(index+1,completeName.length());
		
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileNameWithPath);

		long beforeCalling = new Date().getTime();
		
		File localFile = new File(TEMP_FILE_PATH_NAME);
		s3Client.getObject(getObjectRequest, localFile);

		long afterAws = new Date().getTime();
		
		FileSystemResource fileSystemResource = new FileSystemResource(Paths.get(TEMP_FILE_PATH_NAME).toFile());
		
		long generateResource = new Date().getTime();
		
		log.info(" the time to prep Obj : "+(beforeCalling-startTime));
		log.info(" the time to get object from aws "+(afterAws-beforeCalling));
		log.info(" the time for creating resource form file : "+(generateResource-afterAws));
		return fileSystemResource;
	}
	
	public Map<String, String> getUrlMap(Map<String, String> nameMap){
		
		Map<String, String> urlMap = new HashMap<>();
		AmazonS3 s3Client = getS3Client();
		
		nameMap.keySet().forEach( fileStoreId -> {
			
			String completeName = nameMap.get(fileStoreId);
			int index = completeName.indexOf('/');
			String bucketName = completeName.substring(0,index);
			String fileNameWithPath = completeName.substring(index+1,completeName.length());
			
			 	GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileNameWithPath);
			    generatePresignedUrlRequest.setMethod(HttpMethod.GET);

			    urlMap.put(fileStoreId,s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
		});
		return urlMap;
	}
	
	private AmazonS3 getS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secretKey)))
				.withRegion(Regions.valueOf(awsRegion)).build();
	}
}
