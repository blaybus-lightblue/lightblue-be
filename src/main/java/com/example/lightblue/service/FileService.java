package com.example.lightblue.service;

import com.example.lightblue.dto.FileUploadResponseDto;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    private final Storage storage;
    
    @Value("${gcp.bucket.name}")
    private String bucketName;

    public FileService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * GCP에 파일 업로드하고 URL 반환 후 로컬 파일 삭제
     */
    public FileUploadResponseDto uploadFile(MultipartFile file) throws IOException {
        // 고유한 파일명 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String contentType = file.getContentType();

        // GCP Storage에 파일 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(contentType)
                .build();
        
        Blob blob = storage.create(blobInfo, file.getBytes());
        
        // 공개 URL 생성
        String publicUrl = getPublicUrl(fileName);
        
        return new FileUploadResponseDto(fileName, publicUrl);
    }

    /**
     * GCP Storage에서 파일 삭제
     */
    public void deleteFile(String fileName) {
        storage.delete(bucketName, fileName);
    }

    /**
     * 파일의 공개 URL 생성
     */
    public String getPublicUrl(String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
    
    /**
     * 로컬 파일 삭제
     */
    private void deleteLocalFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
