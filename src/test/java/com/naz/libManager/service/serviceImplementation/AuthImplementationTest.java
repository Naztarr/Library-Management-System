package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.LoginDto;
import com.naz.libManager.dto.SignupDto;
import com.naz.libManager.entity.User;
import com.naz.libManager.enums.Role;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.LoginResponse;
import com.naz.libManager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The AuthImplementationTest class contains test cases for the AuthImplementation service class.
 */
@ExtendWith(SpringExtension.class)
class AuthImplementationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtImplementation jwtImplementation;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthImplementation authImplementation;


    /**
     * Creates a mock user object for testing purposes.
     *
     * @return A mock User object.
     */
    private User createUser(){
        User user = new User();
        user.setEmailAddress("Naz@gmail.com");
        user.setFirstName("Naz");
        user.setLastName("Star");
        return user;
    }

    /**
     * Tests the successful signup operation.
     */
    @Test
    void signup_Success() {
        SignupDto signupDto = new SignupDto("Chinaza", "Herbert",
                "herbertemmanuel116@gmail.com", "09037136349",
                "Naza@123", "Naza@123", Role.PATRON);
        when(userRepository.findByEmailAddress(signupDto.emailAddress())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<String>> response = authImplementation.signup(signupDto);
        assertNotNull(response);
        assertEquals(String.format("Welcome! '%s'. You have successfully signed up", signupDto.firstName()),
                response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatusCode());
    }

    /**
     * Tests the case where the provided email address already exists during signup.
     */
    @Test
    void signup_EmailAlreadyExists() {
        SignupDto signupDto = new SignupDto("Chinaza", "Herbert",
                "herbertemmanuel116@gmail.com", "09037136349",
                "Naza@123", "Naza@123", Role.PATRON);
        when(userRepository.findByEmailAddress(signupDto.emailAddress())).thenReturn(Optional.of(mock(User.class)));
        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> authImplementation.signup(signupDto));
        assertEquals("Email Address already exists", exception.getMessage());
    }

    /**
     * Tests the case where the provided passwords do not match during signup.
     */
    @Test
    void signup_PasswordMisMatch() {
        SignupDto signupDto = new SignupDto("Chinaza", "Herbert",
                "herbertemmanuel116@gmail.com", "09037136349",
                "Naza@123", "Naza@122", Role.PATRON);
        when(userRepository.findByEmailAddress(signupDto.emailAddress())).thenReturn(Optional.empty());
        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> authImplementation.signup(signupDto));
        assertEquals("Provided passwords do not match", exception.getMessage());
    }

    /**
     * Tests the successful login operation.
     */
    @Test
    void login_Success() {
        User user = createUser();
        user.setPassword(passwordEncoder.encode("naztarr$"));
        LoginDto loginDto = new LoginDto("Naz@gmail.com", "naztarr$");

        when(userRepository.findByEmailAddress(loginDto.emailAddress())).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse<LoginResponse>> response = authImplementation.login(loginDto);
        assertNotNull(response);
        assertEquals(String.format("Welcome back '%s'. You are now logged in", user.getFirstName()),
                response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatusCode());

    }

    /**
     * Tests the case where the user is not found during login.
     */
    @Test
    void login_UserNotFound() {
        LoginDto loginDto = new LoginDto("Naz@gmail.com", "naztarr$");

        when(userRepository.findByEmailAddress(loginDto.emailAddress())).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> authImplementation.login(loginDto));
        assertEquals("User not found", exception.getMessage());

    }
}