package org.example.lms_original.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId; // NEW
    private String courseName; // NEW
    private BigDecimal amount;
    private Integer lessonsCount; // NEW
    private BigDecimal pricePerLesson; // NEW
    private LocalDateTime paymentDate;
    private String description;
    private PaymentStatus status;
}