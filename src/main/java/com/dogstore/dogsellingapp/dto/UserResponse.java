package com.dogstore.dogsellingapp.dto;

import com.dogstore.dogsellingapp.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private boolean verified;
}
