package org.example.lms_original.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBalanceDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private Integer remainingLessons;
    private BigDecimal paidAmount;
    private BigDecimal pricePerLesson;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}