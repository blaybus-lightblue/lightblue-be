package com.example.lightblue.service;

import com.example.lightblue.dto.CompanyCreateRequest;
import com.example.lightblue.model.Account;
import com.example.lightblue.model.Company;
import com.example.lightblue.repository.AccountRepository;
import com.example.lightblue.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Company createCompany(CompanyCreateRequest companyDetails) {
        Account account = accountRepository.findById(companyDetails.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id " + companyDetails.getAccountId()));

        // Check if a Company already exists for this Account
        if (account.getCompany() != null) {
            throw new RuntimeException("Company already exists for this account.");
        }

        // Set accountType to COMPANY
        account.setAccountType("COMPANY");
        accountRepository.save(account);

        Company company = new Company();
        company.setAccount(account);
        company.setName(companyDetails.getName());
        company.setEmail(companyDetails.getEmail());
        company.setPhone(companyDetails.getPhone());
        company.setDescription(companyDetails.getDescription());

        return companyRepository.save(company);
    }
}
