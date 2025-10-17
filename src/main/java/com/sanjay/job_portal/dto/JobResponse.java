package com.sanjay.job_portal.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private String jobType;
    private String requiredSkills;
    private String experienceLevel;
    private String salaryMin;
    private String salaryMax;
    private String requirements;
    private String responsibilities;
    private String applicationDeadline;
    private Integer openings;
    private String status;
    private Long employerId;
    private String employerName;
    private String createdAt;
    private String updatedAt;
    private Integer totalApplications;
}
