package com.aitrich.JobPortalSystem.Repository;

import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJobSeekerRepo extends JpaRepository<JobSeeker, Long> {

    JobSeeker findByEmail(String email);

    Optional<JobSeeker> findOptionalByEmail(String email);

    boolean existsByEmail(String email);

    List<JobSeeker> findByActive(boolean active);

    Page<JobSeeker> findAll(Pageable pageable);

    Page<JobSeeker> findByActive(boolean active, Pageable pageable);

    @Query("""
       SELECT j FROM JobSeeker j
       WHERE LOWER(j.location)
       LIKE LOWER(CONCAT('%', :location, '%'))
       AND j.active = true
       """)
    List<JobSeeker> findByLocation(@Param("location") String location);

    @Query("SELECT j FROM JobSeeker j WHERE :skill MEMBER OF j.skills AND j.active = true")
    List<JobSeeker> findBySkill(@Param("skill") String skill);

    @Query("SELECT j FROM JobSeeker j WHERE LOWER(j.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(j.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<JobSeeker> findByNameContaining(@Param("name") String name);

    long countByActive(boolean active);

    @Query("SELECT j FROM JobSeeker j WHERE j.resumeUrl IS NOT NULL AND j.active = true")
    List<JobSeeker> findJobSeekersWithResume();

    @Query("SELECT j FROM JobSeeker j WHERE j.resumeUrl IS NULL AND j.active = true")
    List<JobSeeker> findJobSeekersWithoutResume();
}
