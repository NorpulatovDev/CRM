package org.example.lms_original.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lms_original.dto.user.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserDto user;

    public JwtResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}
