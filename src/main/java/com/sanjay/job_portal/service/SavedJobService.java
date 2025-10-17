package com.sanjay.job_portal.service;

import com.sanjay.job_portal.dto.JobResponse;
import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.SavedJob;
import com.sanjay.job_portal.entity.User;
import com.sanjay.job_portal.repository.JobRepository;
import com.sanjay.job_portal.repository.SavedJobRepository;
import com.sanjay.job_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SavedJobService {

    @Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobService jobService;

    @Transactional
    public void saveJob(Long jobId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.existsByJobAndUser(job, user)) {
            throw new RuntimeException("Job already saved");
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setJob(job);
        savedJob.setUser(user);

        savedJobRepository.save(savedJob);
    }

    @Transactional
    public void unsaveJob(Long jobId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        savedJobRepository.deleteByJobAndUser(job, user);
    }

    public Page<JobResponse> getSavedJobs(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findByUser(user, pageable)
                .map(savedJob -> jobService.getJobById(savedJob.getJob().getId()));
    }

    public boolean isJobSaved(Long jobId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return savedJobRepository.existsByJobAndUser(job, user);
    }
}