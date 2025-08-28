package org.example.lms_original.service;

import org.example.lms_original.dto.teacher.*;
import org.example.lms_original.dto.user.UserDto;
import org.example.lms_original.entity.Role;
import org.example.lms_original.entity.Teacher;
import org.example.lms_original.entity.User;
import org.example.lms_original.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    private final UserService userService;

    public TeacherDto createTeacher(CreateTeacherRequest request) {
        // Ensure the user is created with TEACHER role
        request.getUserRequest().setRole(Role.TEACHER);
        UserDto userDto = userService.createUser(request.getUserRequest());

        Teacher teacher = new Teacher();
        User user = new User();
        user.setId(userDto.getId());
        teacher.setUser(user);
        teacher.setSpecialization(request.getSpecialization());
        teacher.setSalary(request.getSalary());

        Teacher saved = teacherRepository.save(teacher);
        return convertToDto(saved);
    }

    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return convertToDto(teacher);
    }

    public TeacherDto getTeacherByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher not found for user"));
        return convertToDto(teacher);
    }

    public TeacherDto updateTeacher(Long id, CreateTeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setSpecialization(request.getSpecialization());
        teacher.setSalary(request.getSalary());

        Teacher updated = teacherRepository.save(teacher);
        return convertToDto(updated);
    }

    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher not found");
        }
        Teacher teacher = teacherRepository.findById(id).get();
        userService.deleteUser(teacher.getUser().getId());
        teacherRepository.deleteById(id);
    }

    private TeacherDto convertToDto(Teacher teacher) {
        UserDto userDto = userService.getUserById(teacher.getUser().getId());
        return new TeacherDto(
                teacher.getId(),
                userDto,
                teacher.getSpecialization(),
                teacher.getSalary()
        );
    }
}