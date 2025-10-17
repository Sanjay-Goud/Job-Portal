package com.sanjay.job_portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Location is required")
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
}