package org.example.lms_original.service;

import org.example.lms_original.dto.attendance.*;
import org.example.lms_original.entity.*;
import org.example.lms_original.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final StudentBalanceService balanceService;

    public List<AttendanceDto> markAttendance(MarkAttendanceRequest request, Authentication auth) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Attendance> attendances = attendanceRepository.findByLessonId(request.getLessonId());
        
        for (Attendance attendance : attendances) {
            Long studentId = attendance.getStudent().getId();
            Boolean isPresent = request.getStudentAttendance().getOrDefault(studentId, false);
            
            attendance.setIsPresent(isPresent);
            attendance.setMarkedAt(LocalDateTime.now());
            attendance.setMarkedBy(user);
            attendance.setNotes(request.getNotes());
            
            attendanceRepository.save(attendance);

            // If student was present and lesson is completed, deduct from balance
            if (isPresent && lesson.getStatus() == LessonStatus.COMPLETED) {
                try {
                    // Find the course associated with this group
                    // Note: You'll need to add a course field to Group entity or create a relationship
                    // For now, I'll assume we can get the course from the group's first course
                    if (lesson.getGroup().getStudents() != null && !lesson.getGroup().getStudents().isEmpty()) {
                        Student student = lesson.getGroup().getStudents().get(0);
                        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                            Long courseId = student.getCourses().get(0).getId();
                            balanceService.deductLesson(studentId, courseId);
                        }
                    }
                } catch (Exception e) {
                    // Log error but don't fail the attendance marking
                    System.err.println("Error deducting lesson balance: " + e.getMessage());
                }
            }
        }

        return attendances.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<AttendanceDto> getAttendanceByLesson(Long lessonId) {
        return attendanceRepository.findByLessonId(lessonId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDto> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AttendanceDto convertToDto(Attendance attendance) {
        return new AttendanceDto(
                attendance.getId(),
                attendance.getLesson().getId(),
                attendance.getLesson().getTopic(),
                attendance.getStudent().getId(),
                attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName(),
                attendance.getIsPresent(),
                attendance.getMarkedAt(),
                attendance.getMarkedBy() != null ? 
                    attendance.getMarkedBy().getFirstName() + " " + attendance.getMarkedBy().getLastName() : null,
                attendance.getNotes()
        );
    }
}