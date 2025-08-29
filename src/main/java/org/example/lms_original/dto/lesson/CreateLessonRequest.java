package org.example.lms_original.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonRequest {
    @NotNull
    private Long groupId;
    
    @NotNull
    private LocalDateTime lessonDate;
    
    @NotBlank
    private String topic;
    
    private String description;
}
