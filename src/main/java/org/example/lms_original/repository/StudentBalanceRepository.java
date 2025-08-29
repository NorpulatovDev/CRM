package org.example.lms_original.repository;

import org.example.lms_original.entity.StudentBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentBalanceRepository extends JpaRepository<StudentBalance, Long> {
    List<StudentBalance> findByStudentId(Long studentId);
    List<StudentBalance> findByCourseId(Long courseId);
    Optional<StudentBalance> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    @Query("SELECT sb FROM StudentBalance sb WHERE sb.remainingLessons > 0")
    List<StudentBalance> findAllWithRemainingLessons();
}
