package com.example.lightblue.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreateRequest {
    private String name;
    private String email;
    private String phone;
    private String description;
    private Long accountId;
}
