package com.example.controller;

import com.example.util.annotation.ApiMessage;
import com.example.dto.request.CompanyRequest;
import com.example.dto.response.CompanyResponse;
import com.example.dto.response.ResultPaginationResponse;
import com.example.entity.Company;
import com.example.exception.DataNoFoundException;
import com.example.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

//    @GetMapping
//    public ResponseEntity<ResultPaginationResponse> getCompanies(
//            @RequestParam(name = "current", defaultValue = "0") Optional<String> current,
//            @RequestParam(name = "pageSize", defaultValue = "5") Optional<String> pageSize
//    ) {
//        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyPage(current, pageSize));
//    }

    @GetMapping
    @ApiMessage("Get User Filter")
    public ResponseEntity<ResultPaginationResponse> getCompanyFilter(
            @Filter Specification<Company> specification, Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyFilter(specification, pageable));
    }

    @GetMapping("{id}")
    @ApiMessage("Get User By Id")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) throws DataNoFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompany(id));
    }

    @PostMapping
    @ApiMessage("Created User Success")
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                companyService.createCompany(request));
    }

    @PutMapping("{id}")
    @ApiMessage("Updated User Success")
    public ResponseEntity<CompanyResponse> updateCompanyById(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request
    ) throws DataNoFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                companyService.updateCompany(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws DataNoFoundException {
        Boolean result = companyService.deletedCompany(id);
        return (result ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build());

    }
}
