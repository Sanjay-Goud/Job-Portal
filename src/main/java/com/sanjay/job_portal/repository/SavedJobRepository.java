package com.sanjay.job_portal.repository;

import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.SavedJob;
import com.sanjay.job_portal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    Page<SavedJob> findByUser(User user, Pageable pageable);

    boolean existsByJobAndUser(Job job, User user);

    Optional<SavedJob> findByJobAndUser(Job job, User user);

    void deleteByJobAndUser(Job job, User user);
}
