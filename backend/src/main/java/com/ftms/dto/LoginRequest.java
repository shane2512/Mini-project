package com.ftms.dto;

// DTO = Data Transfer Object
// LoginRequest.java is not a database table. It is just a container to receive JSON data from frontend.
// When frontend sends { "email": "user@test.com", "password": "123" }, Spring maps it to this class.

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
