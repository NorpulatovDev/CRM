package org.example.lms_original.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.lms_original.dto.attendance.AttendanceDto;
import org.example.lms_original.dto.balance.StudentBalanceDto;
import org.example.lms_original.dto.lesson.LessonDto;
import org.example.lms_original.dto.teacher.*;
import org.example.lms_original.dto.student.*;
import org.example.lms_original.dto.course.*;
import org.example.lms_original.dto.group.*;
import org.example.lms_original.dto.payment.*;
import org.example.lms_original.dto.user.UserDto;
import org.example.lms_original.entity.PaymentStatus;
import org.example.lms_original.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Admin endpoints for managing users, teachers, students, courses, groups, payments and financial reports")
public class AdminController {

    private final UserService userService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final PaymentService paymentService;
    private final FinancialService financialService;
    private final StudentBalanceService balanceService;
    private final AttendanceService attendanceService;
    private final LessonService lessonService;

    // ================== USER MANAGEMENT ==================

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ================== TEACHER MANAGEMENT ==================

    @Operation(summary = "Create teacher", description = "Add a new teacher")
    @PostMapping("/teachers")
    public ResponseEntity<TeacherDto> createTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        TeacherDto teacher = teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @Operation(summary = "Get all teachers", description = "Retrieve a list of all teachers")
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @Operation(summary = "Get teacher by ID", description = "Retrieve a teacher by their ID")
    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @Operation(summary = "Update teacher", description = "Update a teacher’s details by ID")
    @PutMapping("/teachers/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable Long id,
                                                    @Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, request));
    }

    @Operation(summary = "Delete teacher", description = "Delete a teacher by ID")
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    // ================== STUDENT MANAGEMENT ==================

    @Operation(summary = "Create student", description = "Add a new student")
    @PostMapping("/students")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentDto student = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(summary = "Get student by ID", description = "Retrieve a student by their ID")
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Update student", description = "Update a student’s details by ID")
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id,
                                                    @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @Operation(summary = "Delete student", description = "Delete a student by ID")
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ================== LESSON MANAGEMENT ==================

    @Operation(summary = "Get lessons by teacher", description = "Get all lessons for a specific teacher")
    @GetMapping("/lessons/teacher/{teacherId}")
    public ResponseEntity<List<LessonDto>> getLessonsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(lessonService.getLessonsByTeacher(teacherId));
    }

//    @Operation(summary = "Get all lessons", description = "Get all lessons in the system")
//    @GetMapping("/lessons")
//    public ResponseEntity<List<LessonDto>> getAllLessons(Authentication auth) {
//        // This would require a method to get all lessons - you can implement this in LessonService
//        return ResponseEntity.ok(lessonService.getAllLessons());
//    }

// ================== ATTENDANCE MANAGEMENT ==================

    @Operation(summary = "Get attendance by student", description = "Get all attendance records for a student")
    @GetMapping("/attendance/student/{studentId}")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

// ================== BALANCE MANAGEMENT ==================

//    @Operation(summary = "Get all balances with remaining lessons", description = "Get all student balances that have remaining lessons")
//    @GetMapping("/balances/active")
//    public ResponseEntity<List<StudentBalanceDto>> getActiveBalances() {
//        return ResponseEntity.ok(balanceService.getAllActiveBalances());
//    }

    // ================== COURSE MANAGEMENT ==================

    @Operation(summary = "Create course", description = "Add a new course")
    @PostMapping("/courses")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseDto course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Get course by ID", description = "Retrieve a course by its ID")
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Update course", description = "Update a course’s details by ID")
    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id,
                                                  @Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @Operation(summary = "Delete course", description = "Delete a course by ID")
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // ================== GROUP MANAGEMENT ==================

    @Operation(summary = "Create group", description = "Add a new group")
    @PostMapping("/groups")
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        GroupDto group = groupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @Operation(summary = "Get all groups", description = "Retrieve a list of all groups")
    @GetMapping("/groups")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @Operation(summary = "Get group by ID", description = "Retrieve a group by its ID")
    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @Operation(summary = "Update group", description = "Update a group’s details by ID")
    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long id,
                                                @Valid @RequestBody CreateGroupRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(id, request));
    }

    @Operation(summary = "Delete group", description = "Delete a group by ID")
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    // ================== PAYMENT MANAGEMENT ==================

    @Operation(summary = "Create payment", description = "Add a new payment")
    @PostMapping("/payments")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentDto payment = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments")
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(summary = "Get payment by ID", description = "Retrieve a payment by its ID")
    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @Operation(summary = "Get payments by student ID", description = "Retrieve all payments made by a specific student")
    @GetMapping("/payments/student/{studentId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }

    @Operation(summary = "Update payment status", description = "Update a payment’s status by ID")
    @PutMapping("/payments/{id}/status")
    public ResponseEntity<PaymentDto> updatePaymentStatus(@PathVariable Long id,
                                                          @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    @Operation(summary = "Delete payment", description = "Delete a payment by ID")
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // ================== FINANCIAL REPORTS ==================

    @Operation(summary = "Get financial summary", description = "Retrieve the financial summary report (income and expenses)")
    @GetMapping("/financial-summary")
    public ResponseEntity<FinancialSummaryDto> getFinancialSummary() {
        return ResponseEntity.ok(financialService.getFinancialSummary());
    }
}
