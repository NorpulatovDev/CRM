package org.example.lms_original.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lms_original.dto.balance.StudentBalanceDto;
import org.example.lms_original.service.StudentBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/balances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Balance Management", description = "Endpoints for managing student lesson balances")
public class BalanceController {

    private final StudentBalanceService balanceService;

    @Operation(summary = "Get student balances", description = "Get all lesson balances for a student")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentBalanceDto>> getStudentBalances(@PathVariable Long studentId) {
        return ResponseEntity.ok(balanceService.getStudentBalances(studentId));
    }

    @Operation(summary = "Get course balances", description = "Get all student balances for a course")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentBalanceDto>> getCourseBalances(@PathVariable Long courseId) {
        return ResponseEntity.ok(balanceService.getCourseBalances(courseId));
    }

    @Operation(summary = "Get or create balance", description = "Get existing balance or create new one")
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<StudentBalanceDto> getOrCreateBalance(@PathVariable Long studentId,
                                                                @PathVariable Long courseId) {
        return ResponseEntity.ok(balanceService.getOrCreateBalance(studentId, courseId));
    }
}