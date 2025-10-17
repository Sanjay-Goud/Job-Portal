package com.sanjay.job_portal.controller;


import com.sanjay.job_portal.dto.ApplicationRequest;
import com.sanjay.job_portal.dto.ApplicationResponse;
import com.sanjay.job_portal.dto.StatusUpdateRequest;
import com.sanjay.job_portal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // Applicant endpoints
    @PostMapping("/applicant/applications")
    @PreAuthorize("hasAuthority('APPLICANT')")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.applyForJob(
                request, authentication.getName()));
    }

    @GetMapping("/applicant/applications")
    @PreAuthorize("hasAuthority('APPLICANT')")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getApplicationsByApplicant(
                authentication.getName(), pageable));
    }

    @DeleteMapping("/applicant/applications/{id}")
    @PreAuthorize("hasAuthority('APPLICANT')")
    public ResponseEntity<Void> withdrawApplication(
            @PathVariable Long id,
            Authentication authentication) {
        applicationService.withdrawApplication(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    // Employer endpoints
    @GetMapping("/employer/applications")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsForMyJobs(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getApplicationsForEmployer(
                authentication.getName(), pageable));
    }

    @GetMapping("/employer/jobs/{jobId}/applications")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getApplicationsForJob(
                jobId, authentication.getName(), pageable));
    }

    @PatchMapping("/employer/applications/{id}/status")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(
                id, request, authentication.getName()));
    }
}