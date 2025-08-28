package org.example.lms_original.dto.teacher;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.dto.user.UserDto;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private Long id;
    private UserDto user;
    private String specialization;
    private BigDecimal salary;


}
