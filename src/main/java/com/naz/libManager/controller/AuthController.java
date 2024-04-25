package com.naz.libManager.controller;

import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.enums.VerifyType;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import com.naz.libManager.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "REST APIs for creating users through signup and for logging users in"
)
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/user")
    @Operation(summary = "signup",
            description = "For signup and creating a user")
    ResponseEntity<ApiResponse<String>> createUser(@RequestBody SignupDto signupDto){
        return ResponseEntity.ok(authenticationService.signup(signupDto).getBody());
    }

    @PostMapping("/login")
    @Operation(summary = "login",
            description = "Authenticates the user with email and password")
    ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authenticationService.login(loginDto).getBody());
    }

    @GetMapping("/email-confirmation/{token}")
    @Operation(summary = "confirm email",
            description = "Verifies the user's email with token sent to the email")
    ResponseEntity<ApiResponse<String>> confirmEmailAddress(@PathVariable String token){
        return authenticationService.confirmEmail(token);
    }

    @GetMapping("/verification-link")
    @Operation(summary = "verification link",
            description = "sends or resend verification link for signup or password reset")
    ResponseEntity<ApiResponse<String>> sendLink(@RequestParam String email, @RequestParam VerifyType type){
        return authenticationService.sendLink(email, type);
    }

    @GetMapping("/password-reset/{token}")
    @Operation(summary = "password reset",
            description = "Resets the password of the user")
    ResponseEntity<ApiResponse<String>> resetPassword(@PathVariable String token, @RequestParam String password,
                                                      @RequestParam String confirmPassword){
        return authenticationService.resetPassword(token, password, confirmPassword);
    }
}