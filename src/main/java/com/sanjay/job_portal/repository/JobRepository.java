package com.sanjay.job_portal.repository;

import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.Job.JobStatus;
import com.sanjay.job_portal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    Page<Job> findByEmployer(User employer, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = :status AND " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchJobs(@Param("status") JobStatus status,
                         @Param("keyword") String keyword,
                         Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = :status AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:skills IS NULL OR LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :skills, '%'))) AND " +
            "(:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel)")
    Page<Job> filterJobs(@Param("status") JobStatus status,
                         @Param("location") String location,
                         @Param("skills") String skills,
                         @Param("experienceLevel") String experienceLevel,
                         Pageable pageable);
}