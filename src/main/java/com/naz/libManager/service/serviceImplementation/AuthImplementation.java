package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.entity.User;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import com.naz.libManager.repository.UserRepository;
import com.naz.libManager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtImplementation jwtImplementation;

    protected String generateToken(User user, Long expiryDate) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("first_name", user.getFirstName());
        claims.put("last_name", user.getLastName());
        return jwtImplementation.generateJwtToken(claims, user.getEmailAddress(), expiryDate);
    }


    /**
     * Registers a new user.
     *
     * @param signupDto The DTO containing user signup information.
     * @return ResponseEntity containing ApiResponse with a message indicating successful signup or error message if the signup failed.
     * @throws LibManagerException if the provided email address already exists or if the passwords do not match.
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto) {
        Optional<User> userOptional = userRepository.findByEmailAddress(signupDto.emailAddress());
        if(userOptional.isPresent()){
            throw new LibManagerException("Email Address already exists");
        } else if(signupDto.password().equals(signupDto.confirmPassword())){
            User user = new User();
            user.setFirstName(signupDto.firstName());
            user.setLastName(signupDto.lastName());
            user.setEmailAddress(signupDto.emailAddress());
            user.setPhoneNumber(signupDto.phoneNumber());
            user.setPassword(passwordEncoder.encode(signupDto.password()));
            user.setRole(signupDto.role());
            userRepository.save(user);
        } else{
            throw new LibManagerException("Provided passwords do not match");
        }
        return ResponseEntity.ok(new ApiResponse<>(String.format("Welcome! '%s'. You have successfully signed up", signupDto.firstName()), HttpStatus.OK));
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginDto The DTO containing user login information.
     * @return ResponseEntity containing ApiResponse with LoginResponse indicating successful login or error message if login failed.
     * @throws LibManagerException if the user with the provided email address is not found.
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto) {
        Optional<User> userOptional = userRepository.findByEmailAddress(loginDto.emailAddress());
        if(userOptional.isPresent()){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.emailAddress(), loginDto.Password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginResponse loginResponse = new LoginResponse(
                    userOptional.get().getFirstName(),
                    userOptional.get().getLastName(),
                    userOptional.get().getEmailAddress(),
                    generateToken(userOptional.get(), null)
            );
            return ResponseEntity.ok(new ApiResponse<>(loginResponse, String.format("Welcome back '%s'. You are now logged in", userOptional.get().getFirstName())));
        } else {
            throw new LibManagerException("User not found");
        }
    }

}
