package com.aitrich.JobPortalSystem.Service.Company;

import com.aitrich.JobPortalSystem.DTO.CompanyRequestDTO;
import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Enums.Role;
import com.aitrich.JobPortalSystem.Repository.ICompanyRepo;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Security.OwnershipUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImp implements ICompanyService {

    private final ICompanyRepo repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepo userRepo;

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.COMPANY);
        userRepo.save(user);

        Company company = modelMapper.map(dto, Company.class);
        company.setPassword(user.getPassword());
        company.setActive(true);

        if (company.getJobs() != null) {
            company.getJobs().forEach(job -> {
                job.setCompany(company);
                job.setPostedDate(LocalDate.now());
                job.setActive(true);
            });
        }
        return convertToDTO(repository.save(company));
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO dto) {
        Company company = repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());

        company.setCompanyName(dto.getCompanyName());
        company.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            company.setPassword(passwordEncoder.encode(dto.getPassword())); // fix: encode on update
        company.setWebsite(dto.getWebsite());
        company.setLocation(dto.getLocation());
        company.setDescription(dto.getDescription());
        company.setIndustry(dto.getIndustry());
        company.setEmployeeCount(dto.getEmployeeCount());
        company.setActive(dto.isActive());
        return convertToDTO(repository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());
        repository.deleteById(id);
    }

    @Override
    public CompanyResponseDTO deactivateCompany(Long id) {
        Company company = repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());
        company.setActive(false);
        return convertToDTO(repository.save(company));
    }

    @Override
    public CompanyResponseDTO activateCompany(Long id) {
        Company company = repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());
        company.setActive(true);
        return convertToDTO(repository.save(company));
    }

    // ── read-only (no ownership check needed) ────────────────────────────────

    @Override public List<CompanyResponseDTO> getAllCompanies() {
        return repository.findAll().stream().map(this::convertToDTO).toList();
    }
    @Override public CompanyResponseDTO getCompanyById(Long id) {
        return convertToDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found")));
    }
    @Override public Page<CompanyResponseDTO> getAllCompaniesPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDTO);
    }
    @Override public List<CompanyResponseDTO> getActiveCompanies() {
        return repository.findByActive(true).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override public List<CompanyResponseDTO> searchCompaniesByName(String name) {
        return repository.findByCompanyNameContaining(name).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override public List<CompanyResponseDTO> getCompaniesByLocation(String location) {
        return repository.findByLocation(location).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override public List<CompanyResponseDTO> getCompaniesByIndustry(String industry) {
        return repository.findByIndustry(industry).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
    @Override public Map<String, Long> getCompanyStatistics() {
        Map<String, Long> s = new HashMap<>();
        s.put("totalCompanies", repository.count());
        s.put("activeCompanies", repository.countByActive(true));
        s.put("inactiveCompanies", repository.countByActive(false));
        return s;
    }
    @Override public List<CompanyResponseDTO> getCompaniesByMostJobs() {
        return repository.findCompaniesByMostJobs().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CompanyResponseDTO convertToDTO(Company c) { return modelMapper.map(c, CompanyResponseDTO.class); }
}
