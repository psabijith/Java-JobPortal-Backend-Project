package com.aitrich.JobPortalSystem.Repository;

import com.aitrich.JobPortalSystem.Entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IJobRepo extends JpaRepository<Job, Long> {

    @Query("""
       SELECT DISTINCT j FROM Job j
       LEFT JOIN j.skills s
       WHERE
       LOWER(j.description) 
       LIKE LOWER(CONCAT('%', :keyword, '%'))

       OR LOWER(j.jobTitle)
       LIKE LOWER(CONCAT('%', :keyword, '%'))

       OR LOWER(j.jobType)
       LIKE LOWER(CONCAT('%', :keyword, '%'))

       OR LOWER(j.location)
       LIKE LOWER(CONCAT('%', :keyword, '%'))

       OR LOWER(j.experience)
       LIKE LOWER(CONCAT('%', :keyword, '%'))

       OR LOWER(s)
       LIKE LOWER(CONCAT('%', :keyword, '%'))
       """)
    List<Job> searchJobs(@Param("keyword")String keyword);

    List<Job> findByActive(boolean active);

    Page<Job> findAll(Pageable pageable);

    Page<Job> findByActive(boolean active, Pageable pageable);

    List<Job> findByCompany_Id(Long companyId);

    List<Job> findByCompany_IdAndActive(Long companyId, boolean active);

    @Query("SELECT j FROM Job j WHERE j.salary BETWEEN :min AND :max")
    List<Job> findBySalaryRange(@Param("min") double min, @Param("max") double max);

    @Query("""
SELECT j FROM Job j
WHERE (j.endDate IS NULL OR j.endDate >= :today)
AND j.active = true
ORDER BY j.postedDate DESC
""")
    List<Job> findActiveJobsNotExpired(@Param("today") LocalDate today);

    @Query("SELECT j FROM Job j WHERE j.postedDate >= :since ORDER BY j.postedDate DESC")
    List<Job> findRecentJobs(@Param("since") LocalDate since);

    @Query("SELECT j FROM Job j WHERE LOWER(j.location) = LOWER(:location) AND j.active = true")
    List<Job> findByLocation(@Param("location") String location);

    @Query("SELECT j FROM Job j WHERE LOWER(j.experience) = LOWER(:experience) AND j.active = true")
    List<Job> findByExperience(@Param("experience") String experience);

    @Query("SELECT j FROM Job j WHERE j.salary >= :minSalary AND j.active = true ORDER BY j.salary DESC")
    List<Job> findJobsWithMinimumSalary(@Param("minSalary") double minSalary);

    long countByActive(boolean active);

    long countByCompany_Id(Long companyId);

    @Query("SELECT j FROM Job j WHERE LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%')) AND j.active = true")
    List<Job> findByJobTitleContaining(@Param("title") String title);

    @Query("SELECT j FROM Job j WHERE LOWER(j.jobType) = LOWER(:jobType) AND j.active = true")
    List<Job> findByJobType(@Param("jobType") String jobType);
}
