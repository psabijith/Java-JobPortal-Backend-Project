package com.aitrich.JobPortalSystem.Repository;

import com.aitrich.JobPortalSystem.Entity.Application;
import com.aitrich.JobPortalSystem.Enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface IApplicationRepo extends JpaRepository<Application, Long> {

    List<Application> findByJob_JobId(Long jobId);

    List<Application> findByJobSeeker_Id(Long id);

    @Transactional
    void deleteByJob_JobId(Long jobId);

    @Query(value = "SELECT * FROM APPLICATION WHERE LOWER(status) = 'approved'", nativeQuery = true)
    List<Application> findByStatus();

    List<Application> findByStatus(Status status);

    Page<Application> findAll(Pageable pageable);

    Page<Application> findByStatus(Status status, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.appliedDate BETWEEN :start AND :end")
    List<Application> findByAppliedDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a FROM Application a WHERE a.appliedDate >= :since ORDER BY a.appliedDate DESC")
    List<Application> findRecentApplications(@Param("since") LocalDate since);

    boolean existsByJobSeeker_IdAndJob_JobId(Long jobSeekerId, Long jobId);

    long countByStatus(Status status);

    long countByJob_JobId(Long jobId);

    long countByJobSeeker_Id(Long jobSeekerId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId")
    List<Application> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId AND a.status = :status")
    List<Application> findByCompanyIdAndStatus(@Param("companyId") Long companyId, @Param("status") Status status);

    @Query("SELECT a FROM Application a ORDER BY a.appliedDate DESC")
    List<Application> findAllOrderByAppliedDateDesc();
}
