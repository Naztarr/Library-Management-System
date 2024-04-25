package com.naz.libManager.controller;

import com.naz.libManager.dto.ChangePasswordDto;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "REST APIs allowing users to manage account details like changing password"
)
public class UserController {
    private final UserService userService;

    @PostMapping("/password-change")
    @Operation(summary = "change password",
            description = "Changes the password of the user")
    ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        return userService.changePassword(changePasswordDto);
    }
}
