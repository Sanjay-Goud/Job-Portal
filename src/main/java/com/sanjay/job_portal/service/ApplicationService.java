package com.sanjay.job_portal.service;


import com.sanjay.job_portal.dto.ApplicationRequest;
import com.sanjay.job_portal.dto.ApplicationResponse;
import com.sanjay.job_portal.dto.StatusUpdateRequest;
import com.sanjay.job_portal.entity.Application;
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

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ApplicationResponse applyForJob(ApplicationRequest request, String applicantEmail) {
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (applicant.getRole() != User.Role.APPLICANT) {
            throw new RuntimeException("Only applicants can apply for jobs");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobAndApplicant(job, applicant)) {
            throw new RuntimeException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeUrl(request.getResumeUrl() != null ?
                request.getResumeUrl() : applicant.getResumeUrl());
        application.setStatus(Application.ApplicationStatus.PENDING);

        application = applicationRepository.save(application);
        return mapEntityToResponse(application);
    }

    public Page<ApplicationResponse> getApplicationsByApplicant(String applicantEmail, Pageable pageable) {
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        return applicationRepository.findByApplicant(applicant, pageable)
                .map(this::mapEntityToResponse);
    }

    public Page<ApplicationResponse> getApplicationsForEmployer(String employerEmail, Pageable pageable) {
        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return applicationRepository.findByJobEmployer(employer, pageable)
                .map(this::mapEntityToResponse);
    }

    public Page<ApplicationResponse> getApplicationsForJob(Long jobId, String employerEmail, Pageable pageable) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("Unauthorized to view applications for this job");
        }

        return applicationRepository.findByJob(job, pageable)
                .map(this::mapEntityToResponse);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId,
                                                       StatusUpdateRequest request,
                                                       String employerEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJob().getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("Unauthorized to update this application");
        }

        application.setStatus(Application.ApplicationStatus.valueOf(request.getStatus()));
        application.setEmployerNotes(request.getEmployerNotes());

        application = applicationRepository.save(application);
        return mapEntityToResponse(application);
    }

    @Transactional
    public void withdrawApplication(Long applicationId, String applicantEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getApplicant().getEmail().equals(applicantEmail)) {
            throw new RuntimeException("Unauthorized to withdraw this application");
        }

        applicationRepository.delete(application);
    }

    private ApplicationResponse mapEntityToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setApplicantId(application.getApplicant().getId());
        response.setApplicantName(application.getApplicant().getFullName());
        response.setApplicantEmail(application.getApplicant().getEmail());
        response.setCoverLetter(application.getCoverLetter());
        response.setResumeUrl(application.getResumeUrl());
        response.setStatus(application.getStatus().name());
        response.setEmployerNotes(application.getEmployerNotes());
        response.setAppliedAt(application.getAppliedAt().toString());
        response.setUpdatedAt(application.getUpdatedAt().toString());

        return response;
    }
}