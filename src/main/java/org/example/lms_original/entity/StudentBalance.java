package org.example.lms_original.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer remainingLessons = 0;

    @Column(nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal pricePerLesson = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastUpdated = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}