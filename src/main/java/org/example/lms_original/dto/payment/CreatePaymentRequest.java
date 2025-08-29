package org.example.lms_original.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.entity.PaymentStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId; // NEW: Payment is for specific course

    @DecimalMin("0.01")
    @NotNull
    private BigDecimal amount;

    @Min(1)
    @NotNull
    private Integer lessonsCount; // NEW: Number of lessons this payment covers

    private String description;

    @NotNull
    private PaymentStatus status;
}