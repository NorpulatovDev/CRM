package org.example.lms_original.controller;

import org.example.lms_original.dto.teacher.TeacherDto;
import org.example.lms_original.dto.student.StudentDto;
import org.example.lms_original.dto.course.CourseDto;
import org.example.lms_original.dto.group.GroupDto;
import org.example.lms_original.dto.payment.PaymentDto;
import org.example.lms_original.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {
    
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final PaymentService paymentService;
    private final UserService userService;

    private TeacherDto getCurrentTeacher(Authentication authentication) {
        String username = authentication.getName();
        var user = userService.getUserByUsername(username);
        return teacherService.getTeacherByUserId(user.getId());
    }

    @GetMapping("/profile")
    public ResponseEntity<TeacherDto> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(getCurrentTeacher(authentication));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getMyCourses(Authentication authentication) {
        TeacherDto teacher = getCurrentTeacher(authentication);
        return ResponseEntity.ok(courseService.getCoursesByTeacher(teacher.getId()));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupDto>> getMyGroups(Authentication authentication) {
        TeacherDto teacher = getCurrentTeacher(authentication);
        return ResponseEntity.ok(groupService.getGroupsByTeacher(teacher.getId()));
    }

    @GetMapping("/groups/{groupId}/students")
    public ResponseEntity<List<StudentDto>> getGroupStudents(@PathVariable Long groupId, 
                                                           Authentication authentication) {
        TeacherDto teacher = getCurrentTeacher(authentication);
        
        // Verify the group belongs to this teacher
        GroupDto group = groupService.getGroupById(groupId);
        if (!group.getTeacherId().equals(teacher.getId())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(studentService.getStudentsByGroup(groupId));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentDto> getStudentDetails(@PathVariable Long studentId, 
                                                       Authentication authentication) {
        TeacherDto teacher = getCurrentTeacher(authentication);
        StudentDto student = studentService.getStudentById(studentId);
        
        // Verify the student belongs to a group taught by this teacher
        if (student.getGroupId() == null) {
            return ResponseEntity.status(403).build();
        }
        
        GroupDto group = groupService.getGroupById(student.getGroupId());
        if (!group.getTeacherId().equals(teacher.getId())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(student);
    }

    @GetMapping("/students/{studentId}/payments")
    public ResponseEntity<List<PaymentDto>> getStudentPayments(@PathVariable Long studentId, 
                                                             Authentication authentication) {
        // First verify access to student
        ResponseEntity<StudentDto> studentResponse = getStudentDetails(studentId, authentication);
        if (studentResponse.getStatusCode().value() != 200) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }
}
