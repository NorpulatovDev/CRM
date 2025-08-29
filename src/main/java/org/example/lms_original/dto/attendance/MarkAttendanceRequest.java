package org.example.lms_original.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkAttendanceRequest {
    @NotNull
    private Long lessonId;
    
    @NotNull
    private Map<Long, Boolean> studentAttendance; // studentId -> isPresent
    
    private String notes;
}
