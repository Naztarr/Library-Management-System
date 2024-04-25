package com.naz.libManager.dto;

public record ChangePasswordDto(String oldPassword, String password, String confirmPassword) {
}
