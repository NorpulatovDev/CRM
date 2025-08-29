// Enhanced PaymentService
package org.example.lms_original.service;

import org.example.lms_original.dto.payment.*;
import org.example.lms_original.entity.*;
import org.example.lms_original.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentBalanceService balanceService;

    public PaymentDto createPayment(CreatePaymentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Calculate price per lesson
        BigDecimal pricePerLesson = request.getAmount()
                .divide(BigDecimal.valueOf(request.getLessonsCount()), 2, RoundingMode.HALF_UP);

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setAmount(request.getAmount());
        payment.setLessonsCount(request.getLessonsCount());
        payment.setPricePerLesson(pricePerLesson);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setDescription(request.getDescription());
        payment.setStatus(request.getStatus());

        Payment saved = paymentRepository.save(payment);

        // If payment is completed, add lessons to student balance
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            balanceService.addLessons(
                    request.getStudentId(),
                    request.getCourseId(),
                    request.getAmount(),
                    request.getLessonsCount()
            );
        }

        return convertToDto(saved);
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDto(payment);
    }

    public PaymentDto updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentStatus oldStatus = payment.getStatus();
        payment.setStatus(status);
        Payment updated = paymentRepository.save(payment);

        // If payment status changed to COMPLETED, add lessons to balance
        if (oldStatus != PaymentStatus.COMPLETED && status == PaymentStatus.COMPLETED) {
            balanceService.addLessons(
                    payment.getStudent().getId(),
                    payment.getCourse().getId(),
                    payment.getAmount(),
                    payment.getLessonsCount()
            );
        }

        return convertToDto(updated);
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    private PaymentDto convertToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getStudent().getId(),
                payment.getStudent().getFirstName() + " " + payment.getStudent().getLastName(),
                payment.getCourse().getId(),
                payment.getCourse().getName(),
                payment.getAmount(),
                payment.getLessonsCount(),
                payment.getPricePerLesson(),
                payment.getPaymentDate(),
                payment.getDescription(),
                payment.getStatus()
        );
    }
}