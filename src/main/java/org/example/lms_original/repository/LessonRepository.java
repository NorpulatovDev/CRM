package org.example.lms_original.repository;

import org.example.lms_original.entity.Lesson;
import org.example.lms_original.entity.LessonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByGroupId(Long groupId);
    List<Lesson> findByGroupIdAndStatus(Long groupId, LessonStatus status);
    List<Lesson> findByGroupIdAndLessonDateBetween(Long groupId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT l FROM Lesson l WHERE l.group.teacher.id = :teacherId")
    List<Lesson> findByTeacherId(Long teacherId);
    
    @Query("SELECT l FROM Lesson l WHERE l.group.teacher.id = :teacherId AND l.status = :status")
    List<Lesson> findByTeacherIdAndStatus(Long teacherId, LessonStatus status);
}