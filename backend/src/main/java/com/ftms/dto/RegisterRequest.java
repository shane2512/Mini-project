package com.ftms.dto;

// RegisterRequest.java receives registration form data from frontend.
// Frontend sends all these fields as JSON in POST /api/auth/register
// NOTE: Role is NOT selected during registration. User selects role after KYC approval.
// Bank details are now MANDATORY during registration.

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String city;
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotBlank(message = "SWIFT code is mandatory")
    @Pattern(regexp = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "SWIFT code must be 8 or 11 characters: 4 letters (bank) + 2 letters (country) + 2 alphanumeric (location) + optional 3 alphanumeric (branch). Example: HDFCINBB or HDFCINBBXXX")
    private String swiftCode;

    @NotBlank(message = "IFSC code is mandatory")
    private String ifscCode;

    private String passportBase64; // Base64 encoded passport image
}
