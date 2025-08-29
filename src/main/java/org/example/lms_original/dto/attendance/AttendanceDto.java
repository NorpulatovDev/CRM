package org.example.lms_original.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long id;
    private Long lessonId;
    private String lessonTopic;
    private Long studentId;
    private String studentName;
    private Boolean isPresent;
    private LocalDateTime markedAt;
    private String markedByName;
    private String notes;
}
