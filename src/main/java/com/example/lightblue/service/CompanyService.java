package com.example.lightblue.service;

import com.example.lightblue.dto.CompanyCreateRequest;
import com.example.lightblue.model.Account;
import com.example.lightblue.model.Company;
import com.example.lightblue.repository.AccountRepository;
import com.example.lightblue.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Company createCompany(CompanyCreateRequest companyDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated.");
        }
        Long accountId = ((Account) authentication.getPrincipal()).getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id " + accountId));

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
