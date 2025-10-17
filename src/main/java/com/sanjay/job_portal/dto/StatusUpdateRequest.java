package com.sanjay.job_portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {
    @NotBlank(message = "Status is required")
    private String status;

    private String employerNotes;
}
