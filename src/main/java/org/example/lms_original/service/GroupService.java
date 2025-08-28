package org.example.lms_original.service;

import org.example.lms_original.dto.group.*;
import org.example.lms_original.entity.Group;
import org.example.lms_original.entity.Teacher;
import org.example.lms_original.repository.GroupRepository;
import org.example.lms_original.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    public GroupDto createGroup(CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setStartTime(request.getStartTime());
        group.setEndTime(request.getEndTime());
        group.setWeekDays(request.getWeekDays());

        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            group.setTeacher(teacher);
        }

        Group saved = groupRepository.save(group);
        return convertToDto(saved);
    }

    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getGroupsByTeacher(Long teacherId) {
        return groupRepository.findByTeacherId(teacherId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return convertToDto(group);
    }

    public GroupDto updateGroup(Long id, CreateGroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setStartTime(request.getStartTime());
        group.setEndTime(request.getEndTime());
        group.setWeekDays(request.getWeekDays());

        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            group.setTeacher(teacher);
        } else {
            group.setTeacher(null);
        }

        Group updated = groupRepository.save(group);
        return convertToDto(updated);
    }

    public void deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Group not found");
        }
        groupRepository.deleteById(id);
    }

    private GroupDto convertToDto(Group group) {
        return new GroupDto(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getStartTime(),
                group.getEndTime(),
                group.getWeekDays(),
                group.getTeacher() != null ? group.getTeacher().getId() : null,
                group.getTeacher() != null ? 
                    group.getTeacher().getUser().getFirstName() + " " + group.getTeacher().getUser().getLastName() : null,
                group.getStudents() != null ? group.getStudents().size() : 0
        );
    }
}
