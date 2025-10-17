package com.sanjay.job_portal.service;

import com.sanjay.job_portal.dto.JobRequest;
import com.sanjay.job_portal.dto.JobResponse;
import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.User;
import com.sanjay.job_portal.repository.ApplicationRepository;
import com.sanjay.job_portal.repository.JobRepository;
import com.sanjay.job_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional
    public JobResponse createJob(JobRequest request, String employerEmail) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getRole() != User.Role.EMPLOYER) {
            throw new RuntimeException("Only employers can post jobs");
        }

        Job job = new Job();
        mapRequestToEntity(request, job);
        job.setEmployer(employer);
        job.setStatus(Job.JobStatus.ACTIVE);

        job = jobRepository.save(job);
        return mapEntityToResponse(job);
    }

    @Transactional
    public JobResponse updateJob(Long jobId, JobRequest request, String employerEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("Unauthorized to update this job");
        }

        mapRequestToEntity(request, job);
        job = jobRepository.save(job);
        return mapEntityToResponse(job);
    }

    @Transactional
    public void deleteJob(Long jobId, String employerEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("Unauthorized to delete this job");
        }

        jobRepository.delete(job);
    }

    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapEntityToResponse(job);
    }

    public Page<JobResponse> getJobsByEmployer(String employerEmail, Pageable pageable) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return jobRepository.findByEmployer(employer, pageable)
                .map(this::mapEntityToResponse);
    }

    public Page<JobResponse> searchAndFilterJobs(String keyword, String location,
                                                 String skills, String experienceLevel,
                                                 Pageable pageable) {
        Page<Job> jobs;

        if (keyword != null && !keyword.isEmpty()) {
            jobs = jobRepository.searchJobs(Job.JobStatus.ACTIVE, keyword, pageable);
        } else if ((location != null && !location.isEmpty()) ||
                (skills != null && !skills.isEmpty()) ||
                (experienceLevel != null && !experienceLevel.isEmpty())) {
            jobs = jobRepository.filterJobs(Job.JobStatus.ACTIVE, location,
                    skills, experienceLevel, pageable);
        } else {
            jobs = jobRepository.findByStatus(Job.JobStatus.ACTIVE, pageable);
        }

        return jobs.map(this::mapEntityToResponse);
    }

    private void mapRequestToEntity(JobRequest request, Job job) {
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());

        if (request.getJobType() != null) {
            job.setJobType(Job.JobType.valueOf(request.getJobType()));
        }

        job.setRequiredSkills(request.getRequiredSkills());
        job.setExperienceLevel(request.getExperienceLevel());

        if (request.getSalaryMin() != null) {
            job.setSalaryMin(new BigDecimal(request.getSalaryMin()));
        }
        if (request.getSalaryMax() != null) {
            job.setSalaryMax(new BigDecimal(request.getSalaryMax()));
        }

        job.setRequirements(request.getRequirements());
        job.setResponsibilities(request.getResponsibilities());

        if (request.getApplicationDeadline() != null) {
            job.setApplicationDeadline(LocalDateTime.parse(request.getApplicationDeadline(), formatter));
        }

        if (request.getOpenings() != null) {
            job.setOpenings(request.getOpenings());
        }

        if (request.getStatus() != null) {
            job.setStatus(Job.JobStatus.valueOf(request.getStatus()));
        }
    }

    private JobResponse mapEntityToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setJobType(job.getJobType() != null ? job.getJobType().name() : null);
        response.setRequiredSkills(job.getRequiredSkills());
        response.setExperienceLevel(job.getExperienceLevel());
        response.setSalaryMin(job.getSalaryMin() != null ? job.getSalaryMin().toString() : null);
        response.setSalaryMax(job.getSalaryMax() != null ? job.getSalaryMax().toString() : null);
        response.setRequirements(job.getRequirements());
        response.setResponsibilities(job.getResponsibilities());
        response.setApplicationDeadline(job.getApplicationDeadline() != null ?
                job.getApplicationDeadline().toString() : null);
        response.setOpenings(job.getOpenings());
        response.setStatus(job.getStatus().name());
        response.setEmployerId(job.getEmployer().getId());
        response.setEmployerName(job.getEmployer().getFullName());
        response.setCreatedAt(job.getCreatedAt().toString());
        response.setUpdatedAt(job.getUpdatedAt().toString());
        response.setTotalApplications((int) applicationRepository.countByJobAndStatus(
                job, com.sanjay.job_portal.entity.Application.ApplicationStatus.PENDING));

        return response;
    }
}