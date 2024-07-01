package com.example.service.impl;

import com.example.dto.request.CompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.Meta;
import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.Company;
import com.example.exception.DataNoFoundException;
import com.example.repository.CompanyRepository;
import com.example.service.CompanyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.dto.response.CompanyResponse.transCompany;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyServiceImpl implements CompanyService {
    CompanyRepository companyRepository;

    @Override
    public ResultPaginationResponse getCompanyPage(Optional<String> current, Optional<String> pageSize) {
        String sCurrent = current.isPresent() ? current.get() : "0";
        String sPageSize = pageSize.isPresent() ? pageSize.get() : "5";

        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        Page<Company> companyPage = companyRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(companyPage.getNumber() + 1)
                .pageSize(companyPage.getSize())
                .pages(companyPage.getTotalPages())
                .total(companyPage.getTotalElements())
                .build();

        ResultPaginationResponse response = ResultPaginationResponse
                .builder()
                .meta(meta)
                .result(companyPage.getContent())
                .build();
        return response;
    }

    @Override
    public ResultPaginationResponse getCompanyFilter(Specification<Company> specification, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(specification, pageable);
        Meta meta = Meta.builder()
                .page(companyPage.getNumber() + 1)
                .pageSize(companyPage.getSize())
                .pages(companyPage.getTotalPages())
                .total(companyPage.getTotalElements())
                .build();

        ResultPaginationResponse response = ResultPaginationResponse
                .builder()
                .meta(meta)
                .result(companyPage.getContent())
                .build();
        return response;
    }

    @Override
    public List<CompanyResponse> getCompanies() {
        return companyRepository.findAll().stream().map(
                company -> transCompany(company)
        ).toList();
    }

    @Override
    public CompanyResponse getCompany(Long id) throws DataNoFoundException {
        Company existingCompany = companyRepository.findById(id).orElseThrow(
                () -> new DataNoFoundException("Cannot find company with id : " + id));

        return transCompany(existingCompany);
    }

    @Override
    public CompanyResponse createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .logo(request.getLogo())
                .description(request.getDescription())
                .build();

        return transCompany(companyRepository.save(company));
    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) throws DataNoFoundException {
        Company existingCompany = companyRepository.findById(id).orElseThrow(
                () -> new DataNoFoundException("Cannot find company with id : " + id));

        existingCompany.setName(request.getName());
        existingCompany.setAddress(request.getAddress());
        existingCompany.setLogo(request.getLogo());
        existingCompany.setDescription(request.getDescription());
        Company response = companyRepository.save(existingCompany);
        return transCompany(response);
    }

    @Override
    public Boolean deletedCompany(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
