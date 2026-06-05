package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.CompanyRequestDTO;
import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.Service.Company.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;


    @PostMapping
    public ResponseEntity<CompanyResponseDTO> saveCompany(
            @Valid @RequestBody CompanyRequestDTO companyDTO) {

        CompanyResponseDTO savedCompany = companyService.createCompany(companyDTO);

        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {

        return ResponseEntity.ok(companyService.getAllCompanies());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(companyService.getCompanyById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequestDTO companyDTO) {

        CompanyResponseDTO updatedCompany =
                companyService.updateCompany(id, companyDTO);

        return ResponseEntity.ok(updatedCompany);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(
            @PathVariable Long id) {

        companyService.deleteCompany(id);

        return ResponseEntity.ok("Company deleted successfully");
    }


    @GetMapping("/paginated")
    public ResponseEntity<Page<CompanyResponseDTO>> getAllCompaniesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                companyService.getAllCompaniesPaginated(pageable)
        );
    }


    @GetMapping("/active")
    public ResponseEntity<List<CompanyResponseDTO>> getActiveCompanies() {

        return ResponseEntity.ok(companyService.getActiveCompanies());
    }


    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponseDTO>> searchCompaniesByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                companyService.searchCompaniesByName(name)
        );
    }


    @GetMapping("/location")
    public ResponseEntity<List<CompanyResponseDTO>> getCompaniesByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(
                companyService.getCompaniesByLocation(location)
        );
    }


    @GetMapping("/industry")
    public ResponseEntity<List<CompanyResponseDTO>> getCompaniesByIndustry(
            @RequestParam String industry) {

        return ResponseEntity.ok(
                companyService.getCompaniesByIndustry(industry)
        );
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<CompanyResponseDTO> deactivateCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                companyService.deactivateCompany(id)
        );
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<CompanyResponseDTO> activateCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                companyService.activateCompany(id)
        );
    }


    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                companyService.existsByEmail(email)
        );
    }


    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getCompanyStatistics() {

        return ResponseEntity.ok(
                companyService.getCompanyStatistics()
        );
    }


    @GetMapping("/most-jobs")
    public ResponseEntity<List<CompanyResponseDTO>> getCompaniesByMostJobs() {

        return ResponseEntity.ok(
                companyService.getCompaniesByMostJobs()
        );
    }
}