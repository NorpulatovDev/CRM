package org.example.lms_original.service;

import org.example.lms_original.dto.lesson.*;
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
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public LessonDto createLesson(CreateLessonRequest request, Authentication auth) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Lesson lesson = new Lesson();
        lesson.setGroup(group);
        lesson.setLessonDate(request.getLessonDate());
        lesson.setTopic(request.getTopic());
        lesson.setDescription(request.getDescription());
        lesson.setStatus(LessonStatus.SCHEDULED);

        Lesson saved = lessonRepository.save(lesson);

        // Create attendance records for all students in the group
        List<Student> students = studentRepository.findByGroupId(group.getId());
        for (Student student : students) {
            Attendance attendance = new Attendance();
            attendance.setLesson(saved);
            attendance.setStudent(student);
            attendance.setIsPresent(false);
            attendanceRepository.save(attendance);
        }

        return convertToDto(saved);
    }

    public List<LessonDto> getLessonsByGroup(Long groupId) {
        return lessonRepository.findByGroupId(groupId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getLessonsByTeacher(Long teacherId) {
        return lessonRepository.findByTeacherId(teacherId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return convertToDto(lesson);
    }

    public LessonDto markLessonCompleted(Long lessonId, Authentication auth) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        if (lesson.getStatus() == LessonStatus.COMPLETED) {
            throw new RuntimeException("Lesson already marked as completed");
        }

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        lesson.setStatus(LessonStatus.COMPLETED);
        lesson.setCompletedAt(LocalDateTime.now());
        lesson.setMarkedBy(user);

        Lesson updated = lessonRepository.save(lesson);
        return convertToDto(updated);
    }

    private LessonDto convertToDto(Lesson lesson) {
        List<Attendance> attendances = attendanceRepository.findByLessonId(lesson.getId());
        int totalStudents = attendances.size();
        int presentStudents = (int) attendances.stream().filter(Attendance::getIsPresent).count();

        return new LessonDto(
                lesson.getId(),
                lesson.getGroup().getId(),
                lesson.getGroup().getName(),
                lesson.getLessonDate(),
                lesson.getTopic(),
                lesson.getDescription(),
                lesson.getStatus(),
                lesson.getCreatedAt(),
                lesson.getCompletedAt(),
                lesson.getMarkedBy() != null ? 
                    lesson.getMarkedBy().getFirstName() + " " + lesson.getMarkedBy().getLastName() : null,
                totalStudents,
                presentStudents
        );
    }
}