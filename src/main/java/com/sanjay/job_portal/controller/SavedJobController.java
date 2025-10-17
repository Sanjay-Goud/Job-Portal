package com.sanjay.job_portal.controller;


import com.sanjay.job_portal.dto.JobResponse;
import com.sanjay.job_portal.service.SavedJobService;
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
@RequestMapping("/api/applicant/saved-jobs")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('APPLICANT')")
public class SavedJobController {

    @Autowired
    private SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    public ResponseEntity<Void> saveJob(
            @PathVariable Long jobId,
            Authentication authentication) {
        savedJobService.saveJob(jobId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> unsaveJob(
            @PathVariable Long jobId,
            Authentication authentication) {
        savedJobService.unsaveJob(jobId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getSavedJobs(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("savedAt").descending());
        return ResponseEntity.ok(savedJobService.getSavedJobs(
                authentication.getName(), pageable));
    }

    @GetMapping("/check/{jobId}")
    public ResponseEntity<Boolean> isJobSaved(
            @PathVariable Long jobId,
            Authentication authentication) {
        return ResponseEntity.ok(savedJobService.isJobSaved(
                jobId, authentication.getName()));
    }
}
