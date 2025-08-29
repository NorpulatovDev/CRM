package org.example.lms_original.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.entity.LessonStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private Long groupId;
    private String groupName;
    private LocalDateTime lessonDate;
    private String topic;
    private String description;
    private LessonStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String markedByName;
    private Integer totalStudents;
    private Integer presentStudents;
}