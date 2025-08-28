package org.example.lms_original.dto.group;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGroupRequest {
    @NotBlank
    private String name;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String weekDays;
    private Long teacherId;
}
