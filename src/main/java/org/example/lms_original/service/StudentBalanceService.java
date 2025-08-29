package org.example.lms_original.service;

import org.example.lms_original.dto.balance.StudentBalanceDto;
import org.example.lms_original.entity.*;
import org.example.lms_original.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentBalanceService {
    
    private final StudentBalanceRepository balanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentBalanceDto getOrCreateBalance(Long studentId, Long courseId) {
        return balanceRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(this::convertToDto)
                .orElseGet(() -> createBalance(studentId, courseId));
    }

    public StudentBalanceDto createBalance(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        StudentBalance balance = new StudentBalance();
        balance.setStudent(student);
        balance.setCourse(course);
        balance.setRemainingLessons(0);
        balance.setPaidAmount(BigDecimal.ZERO);
        balance.setPricePerLesson(BigDecimal.ZERO);

        StudentBalance saved = balanceRepository.save(balance);
        return convertToDto(saved);
    }

    public StudentBalanceDto addLessons(Long studentId, Long courseId, BigDecimal amount, Integer lessonsCount) {
        StudentBalance balance = balanceRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseGet(() -> {
                    StudentBalance newBalance = new StudentBalance();
                    Student student = studentRepository.findById(studentId)
                            .orElseThrow(() -> new RuntimeException("Student not found"));
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new RuntimeException("Course not found"));
                    newBalance.setStudent(student);
                    newBalance.setCourse(course);
                    newBalance.setRemainingLessons(0);
                    newBalance.setPaidAmount(BigDecimal.ZERO);
                    newBalance.setPricePerLesson(BigDecimal.ZERO);
                    return newBalance;
                });

        // Calculate price per lesson
        BigDecimal pricePerLesson = amount.divide(BigDecimal.valueOf(lessonsCount), 2, RoundingMode.HALF_UP);
        
        // Update balance
        balance.setRemainingLessons(balance.getRemainingLessons() + lessonsCount);
        balance.setPaidAmount(balance.getPaidAmount().add(amount));
        balance.setPricePerLesson(pricePerLesson);

        StudentBalance updated = balanceRepository.save(balance);
        return convertToDto(updated);
    }

    public StudentBalanceDto deductLesson(Long studentId, Long courseId) {
        StudentBalance balance = balanceRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Student balance not found"));

        if (balance.getRemainingLessons() <= 0) {
            throw new RuntimeException("No remaining lessons for this student");
        }

        balance.setRemainingLessons(balance.getRemainingLessons() - 1);
        StudentBalance updated = balanceRepository.save(balance);
        return convertToDto(updated);
    }

    public List<StudentBalanceDto> getStudentBalances(Long studentId) {
        return balanceRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<StudentBalanceDto> getCourseBalances(Long courseId) {
        return balanceRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private StudentBalanceDto convertToDto(StudentBalance balance) {
        return new StudentBalanceDto(
                balance.getId(),
                balance.getStudent().getId(),
                balance.getStudent().getFirstName() + " " + balance.getStudent().getLastName(),
                balance.getCourse().getId(),
                balance.getCourse().getName(),
                balance.getRemainingLessons(),
                balance.getPaidAmount(),
                balance.getPricePerLesson(),
                balance.getCreatedAt(),
                balance.getLastUpdated()
        );
    }
}