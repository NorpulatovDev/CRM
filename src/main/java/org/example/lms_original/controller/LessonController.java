package org.example.lms_original.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lms_original.dto.lesson.*;
import org.example.lms_original.dto.attendance.*;
import org.example.lms_original.service.LessonService;
import org.example.lms_original.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lessons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
@Tag(name = "Lesson Management", description = "Endpoints for managing lessons")
public class LessonController {

    private final LessonService lessonService;
    private final AttendanceService attendanceService;

    @Operation(summary = "Create lesson", description = "Create a new lesson for a group")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDto> createLesson(@Valid @RequestBody CreateLessonRequest request,
                                                  Authentication auth) {
        LessonDto lesson = lessonService.createLesson(request, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    @Operation(summary = "Get lessons by group", description = "Get all lessons for a specific group")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<LessonDto>> getLessonsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(lessonService.getLessonsByGroup(groupId));
    }

    @Operation(summary = "Get lesson by ID", description = "Get lesson details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @Operation(summary = "Mark lesson as completed", description = "Mark a lesson as completed")
    @PutMapping("/{id}/complete")
    public ResponseEntity<LessonDto> markLessonCompleted(@PathVariable Long id,
                                                         Authentication auth) {
        return ResponseEntity.ok(lessonService.markLessonCompleted(id, auth));
    }

    @Operation(summary = "Mark attendance", description = "Mark attendance for a lesson")
    @PostMapping("/attendance")
    public ResponseEntity<List<AttendanceDto>> markAttendance(@Valid @RequestBody MarkAttendanceRequest request,
                                                              Authentication auth) {
        return ResponseEntity.ok(attendanceService.markAttendance(request, auth));
    }

    @Operation(summary = "Get attendance by lesson", description = "Get attendance records for a lesson")
    @GetMapping("/{lessonId}/attendance")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByLesson(lessonId));
    }
}