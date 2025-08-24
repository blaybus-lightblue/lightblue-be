package com.example.lightblue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LightblueApplication implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(LightblueApplication.class);

	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String naverRedirectUri;

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@Value("${gcp.bucket.name}")
	private String gcpBucketName;

	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	public static void main(String[] args) {
		SpringApplication.run(LightblueApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("==================== Lightblue Configuration ====================");
		logger.info("Naver OAuth2 Redirect URI : {}", naverRedirectUri);
		logger.info("Datasource URL            : {}", datasourceUrl);
		logger.info("GCP Bucket Name           : {}", gcpBucketName);
		logger.info("JWT Expiration (ms)       : {}", jwtExpiration);
		logger.info("=================================================================");
	}
}
