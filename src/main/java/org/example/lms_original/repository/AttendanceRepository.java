package org.example.lms_original.repository;

import org.example.lms_original.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLessonId(Long lessonId);
    List<Attendance> findByStudentId(Long studentId);
    Optional<Attendance> findByLessonIdAndStudentId(Long lessonId, Long studentId);
    
    @Query("SELECT a FROM Attendance a WHERE a.lesson.id = :lessonId AND a.isPresent = true")
    List<Attendance> findPresentStudentsByLesson(Long lessonId);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.isPresent = true")
    Long countAttendedLessonsByStudent(Long studentId);
}