package com.sanjay.job_portal.repository;

import com.sanjay.job_portal.entity.Application;
import com.sanjay.job_portal.entity.Application.ApplicationStatus;
import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByApplicant(User applicant, Pageable pageable);

    Page<Application> findByJob(Job job, Pageable pageable);

    Page<Application> findByJobEmployer(User employer, Pageable pageable);

    boolean existsByJobAndApplicant(Job job, User applicant);

    Optional<Application> findByJobAndApplicant(Job job, User applicant);

    long countByJobAndStatus(Job job, ApplicationStatus status);
}