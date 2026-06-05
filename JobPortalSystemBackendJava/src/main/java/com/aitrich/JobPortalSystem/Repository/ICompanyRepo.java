package com.aitrich.JobPortalSystem.Repository;

import com.aitrich.JobPortalSystem.Entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICompanyRepo extends JpaRepository<Company, Long> {

    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Company> findByActive(boolean active);

    Page<Company> findAll(Pageable pageable);

    @Query("SELECT c FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Company> findByCompanyNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Company c WHERE LOWER(c.location) = LOWER(:location) AND c.active = true")
    List<Company> findByLocation(@Param("location") String location);

    @Query("SELECT c FROM Company c WHERE LOWER(c.industry) = LOWER(:industry) AND c.active = true")
    List<Company> findByIndustry(@Param("industry") String industry);

    long countByActive(boolean active);

    @Query("SELECT c FROM Company c ORDER BY SIZE(c.jobs) DESC")
    List<Company> findCompaniesByMostJobs();
}
