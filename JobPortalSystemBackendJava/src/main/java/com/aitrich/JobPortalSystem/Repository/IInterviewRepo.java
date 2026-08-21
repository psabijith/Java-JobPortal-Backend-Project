package com.aitrich.JobPortalSystem.Repository;

import com.aitrich.JobPortalSystem.Entity.Interview;
import com.aitrich.JobPortalSystem.Enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInterviewRepo extends JpaRepository<Interview, Long> {

    List<Interview> findByApplication_Id(Long applicationId);

    List<Interview> findByApplication_JobSeeker_Id(Long jobSeekerId);

    @Query("SELECT i FROM Interview i WHERE i.application.job.company.id = :companyId")
    List<Interview> findByCompanyId(@Param("companyId") Long companyId);

    List<Interview> findByStatus(InterviewStatus status);
}
