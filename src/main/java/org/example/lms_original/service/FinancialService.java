package org.example.lms_original.service;

import org.example.lms_original.dto.payment.FinancialSummaryDto;
import org.example.lms_original.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinancialService {
    
    private final PaymentRepository paymentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public FinancialSummaryDto getFinancialSummary() {
        BigDecimal totalIncome = paymentRepository.getTotalCompletedPayments();
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        
        BigDecimal totalExpenses = teacherRepository.getTotalSalaries();
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;
        
        BigDecimal netProfit = totalIncome.subtract(totalExpenses);
        
        int totalStudents = (int) studentRepository.count();
        int totalTeachers = (int) teacherRepository.count();
        int totalCourses = (int) courseRepository.count();
        
        return new FinancialSummaryDto(
                totalIncome,
                totalExpenses,
                netProfit,
                totalStudents,
                totalTeachers,
                totalCourses
        );
    }
}