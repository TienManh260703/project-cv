package com.example.service;

import com.example.dto.request.CompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.Company;
import com.example.exception.DataNoFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    ResultPaginationResponse getCompanyPage(Optional<String> current, Optional<String> pageSize);

    ResultPaginationResponse getCompanyFilter(Specification<Company> specification , Pageable pageable);

    List<CompanyResponse> getCompanies();

    CompanyResponse getCompany(Long id) throws DataNoFoundException;

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request) throws DataNoFoundException;

    Boolean deletedCompany(Long id) throws DataNoFoundException;
}
