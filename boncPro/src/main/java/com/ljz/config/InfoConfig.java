package com.ljz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="info",ignoreUnknownFields=false)
@PropertySource("classpath:info.properties")
@Component
public class InfoConfig {
	
	private String filePath;
	
	private String fileEncode;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileEncode() {
		return fileEncode;
	}

	public void setFileEncode(String fileEncode) {
		this.fileEncode = fileEncode;
	}
	
	

	
	
	

}
