package com.naz.libManager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(@NotBlank(message = "firstName is required")
                          String firstName,
                          @NotBlank(message = "lastName is required")
                          String lastName,

                          @NotBlank(message = "Email address is required")
                          @Email(message = "Invalid email address format")
                          String emailAddress,

                          @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
                          String phoneNumber
                          ) {
}
