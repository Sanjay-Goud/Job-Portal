package com.sanjay.job_portal.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // EMPLOYER or APPLICANT

    private String company; // For employers

    private String skills; // Comma-separated skills for applicants

    private String experience; // Years of experience

    private String resumeUrl; // URL to uploaded resume

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String location;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean enabled = true;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private Set<Job> postedJobs = new HashSet<>();

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SavedJob> savedJobs = new HashSet<>();

    public enum Role {
        EMPLOYER, APPLICANT
    }
}