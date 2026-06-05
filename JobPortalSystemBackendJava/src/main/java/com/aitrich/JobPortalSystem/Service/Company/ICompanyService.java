package com.aitrich.JobPortalSystem.Service.Company;

import com.aitrich.JobPortalSystem.DTO.CompanyRequestDTO;
import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ICompanyService {




        CompanyResponseDTO createCompany(CompanyRequestDTO dto);


        List<CompanyResponseDTO> getAllCompanies();


        CompanyResponseDTO getCompanyById(Long id);


        CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO updatedCompanyDTO);


        void deleteCompany(Long id);


        Page<CompanyResponseDTO> getAllCompaniesPaginated(Pageable pageable);


        List<CompanyResponseDTO> getActiveCompanies();


        List<CompanyResponseDTO> searchCompaniesByName(String name);


        List<CompanyResponseDTO> getCompaniesByLocation(String location);


        List<CompanyResponseDTO> getCompaniesByIndustry(String industry);


        CompanyResponseDTO deactivateCompany(Long id);


        CompanyResponseDTO activateCompany(Long id);


        boolean existsByEmail(String email);


        Map<String, Long> getCompanyStatistics();


        List<CompanyResponseDTO> getCompaniesByMostJobs();
    }

