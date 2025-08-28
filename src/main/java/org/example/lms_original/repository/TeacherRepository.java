package org.example.lms_original.repository;

import org.example.lms_original.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);
    
    @Query("SELECT SUM(t.salary) FROM Teacher t")
    BigDecimal getTotalSalaries();
}