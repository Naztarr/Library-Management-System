package com.naz.libManager.controller;

import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import com.naz.libManager.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping("/email-confirmation")
    @Operation(summary = "confirm email",
            description = "Verifies the user's email with token sent to the email")
    ResponseEntity<ApiResponse<String>> confirmEmailAddress(@PathVariable String token){
        return authenticationService.confirmEmail(token);
    }
}
