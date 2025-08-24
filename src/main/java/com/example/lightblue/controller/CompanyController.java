package com.example.lightblue.controller;

import com.example.lightblue.dto.CompanyCreateRequest;
import com.example.lightblue.dto.CompanyDTO;
import com.example.lightblue.global.ApiResponse;
import com.example.lightblue.model.Company;
import com.example.lightblue.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "법인 계정 관련 API")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @Operation(summary = "법인 계정으로 전환", description = "기업 정보를 등록합니다.")
    public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(@RequestBody CompanyCreateRequest companyRequest) {
        Company createdCompany = companyService.createCompany(companyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(new CompanyDTO(createdCompany)));
    }
}
