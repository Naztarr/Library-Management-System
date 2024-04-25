package com.naz.libManager.service;

import com.naz.libManager.dto.ChangePasswordDto;
import com.naz.libManager.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordDto changePasswordDto);
}
