package com.naz.libManager.dto;

import com.naz.libManager.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupDto(@NotBlank(message = "firstName is required")
                        String firstName,
                        @NotBlank(message = "lastName is required")
                        String lastName,

                        @NotBlank(message = "Email address is required")
                        @Email(message = "Invalid email address format")
                        String emailAddress,

                        @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
                        String phoneNumber,
                        @NotBlank(message = "Password is required")
                        @Size(min = 8, message = "Password must be at least 8 characters long")
                        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[a-z]).{8,}$",
                                message = "Password must contain at least one uppercase letter, one special character, and one lowercase letter")
                        String password,
                        String confirmPassword,

                        Role role) {
}
