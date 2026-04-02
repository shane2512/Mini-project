package com.ftms.dto;

import com.ftms.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String city;
    private String address;
    private String phone;
    private String bankName;
    private String accountNumber;
    private String swiftCode;
    private String ifscCode;
    private User.Role role;
}
