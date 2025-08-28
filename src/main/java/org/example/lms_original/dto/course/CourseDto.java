package org.example.lms_original.dto.course;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private Long teacherId;
    private String teacherName;
    private Integer studentCount;
}
