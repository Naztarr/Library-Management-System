package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.ChangePasswordDto;
import com.naz.libManager.entity.User;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.repository.UserRepository;
import com.naz.libManager.service.UserService;
import com.naz.libManager.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @param changePasswordDto Request body containing the current and new passwords of the user
     * @return ResponseEntity containing ApiResponse indicating the status of the request
     */
    @Override
    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser()).orElseThrow(()
                -> new LibManagerException("User not found"));
        if(passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())){
            if(changePasswordDto.password().equals(changePasswordDto.confirmPassword())){
                user.setPassword(passwordEncoder.encode(changePasswordDto.password()));
                userRepository.save(user);
                return ResponseEntity.ok(new ApiResponse<>("Password changed successfully", HttpStatus.OK));
            } else{
                throw new LibManagerException("New passwords do not match");
            }
        } else{
            throw new LibManagerException("Incorrect old password");
        }
    }
}
