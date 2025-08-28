package org.example.lms_original.dto.course;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCourseRequest {
    @NotBlank
    private String name;

    private String description;

    @Min(1)
    private Integer duration;

    @DecimalMin("0.0")
    private BigDecimal price;

    private Long teacherId;
}
