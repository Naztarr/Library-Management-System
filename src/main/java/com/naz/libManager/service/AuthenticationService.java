package com.naz.libManager.service;

import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.enums.VerifyType;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto);
    ResponseEntity<ApiResponse<String>> confirmEmail(String token);
    ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto);
    ResponseEntity<ApiResponse<String>> sendLink(String email, VerifyType type);
    ResponseEntity<ApiResponse<String>> resetPassword(String token, String password, String confirmPassword );
}
