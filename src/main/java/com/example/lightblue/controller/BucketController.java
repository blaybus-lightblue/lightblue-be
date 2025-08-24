package com.example.lightblue.controller;

import com.example.lightblue.dto.FileDeleteRequestDto;
import com.example.lightblue.dto.FileUploadResponseDto;
import com.example.lightblue.global.ApiResponse;
import com.example.lightblue.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class BucketController {

    private final FileService fileService;

    /**
     * 파일 업로드 - GCP에 업로드하고 URL 반환
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<FileUploadResponseDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadResponseDto response = fileService.uploadFile(file);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 파일 삭제 - GCP에서 파일 삭제
     */
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteFile(@RequestBody FileDeleteRequestDto request) {
        fileService.deleteFile(request.getFileName());
        return ApiResponse.onSuccess("Deleted: " + request.getFileName());
    }
}
