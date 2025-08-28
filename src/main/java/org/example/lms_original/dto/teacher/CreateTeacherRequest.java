package org.example.lms_original.dto.teacher;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.dto.user.CreateUserRequest;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherRequest {
    @NotNull
    private CreateUserRequest userRequest;

    private String specialization;

    @DecimalMin("0.0")
    private BigDecimal salary;
}
