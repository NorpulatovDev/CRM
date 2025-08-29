package org.example.lms_original.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lms_original.dto.lesson.LessonDto;
import org.example.lms_original.dto.attendance.*;
import org.example.lms_original.service.LessonService;
import org.example.lms_original.service.AttendanceService;
import org.example.lms_original.service.TeacherService;
import org.example.lms_original.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('TEACHER')")
@Tag(name = "Teacher Lesson Management", description = "Endpoints for teachers to manage their lessons")
public class TeacherLessonController {

    private final LessonService lessonService;
    private final AttendanceService attendanceService;
    private final TeacherService teacherService;
    private final UserService userService;

    @Operation(summary = "Get my lessons", description = "Get all lessons for the current teacher")
    @GetMapping
    public ResponseEntity<List<LessonDto>> getMyLessons(Authentication auth) {
        var user = userService.getUserByUsername(auth.getName());
        var teacher = teacherService.getTeacherByUserId(user.getId());
        return ResponseEntity.ok(lessonService.getLessonsByTeacher(teacher.getId()));
    }

    @Operation(summary = "Mark lesson as completed", description = "Mark my lesson as completed")
    @PutMapping("/{id}/complete")
    public ResponseEntity<LessonDto> markLessonCompleted(@PathVariable Long id,
                                                         Authentication auth) {
        return ResponseEntity.ok(lessonService.markLessonCompleted(id, auth));
    }

    @Operation(summary = "Mark attendance", description = "Mark attendance for my lesson")
    @PostMapping("/attendance")
    public ResponseEntity<List<AttendanceDto>> markAttendance(@Valid @RequestBody MarkAttendanceRequest request,
                                                              Authentication auth) {
        return ResponseEntity.ok(attendanceService.markAttendance(request, auth));
    }

    @Operation(summary = "Get attendance by lesson", description = "Get attendance records for my lesson")
    @GetMapping("/{lessonId}/attendance")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByLesson(lessonId));
    }
}