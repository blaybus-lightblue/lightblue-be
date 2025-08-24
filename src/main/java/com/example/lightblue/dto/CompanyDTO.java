package com.example.lightblue.dto;

import com.example.lightblue.model.Company;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
    private Long id;
    private Long accountId;
    private String name;
    private String email;
    private String phone;
    private String description;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.accountId = company.getAccount().getId();
        this.name = company.getName();
        this.email = company.getEmail();
        this.phone = company.getPhone();
        this.description = company.getDescription();
    }
}
