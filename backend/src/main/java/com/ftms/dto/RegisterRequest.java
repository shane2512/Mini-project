package com.ftms.dto;

// RegisterRequest.java receives registration form data from frontend.
// Frontend sends all these fields as JSON in POST /api/auth/register
// NOTE: Role is NOT selected during registration. User selects role after KYC approval.

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
    private String passportBase64; // Base64 encoded passport image
}
