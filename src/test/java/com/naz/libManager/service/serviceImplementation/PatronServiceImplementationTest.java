package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.entity.User;
import com.naz.libManager.enums.Role;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;
import com.naz.libManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PatronServiceImplementationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PatronServiceImplementation patronService;

    /**
     * Setup method executed before each test case.
     * Initializes the mocks and sets up a mock security context.
     */
    @BeforeEach
    void setUp() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("Naz@gmail.com", "naztarr$");
        securityContext.setAuthentication(authentication);
    }

    /**
     * Method to create a mock User instance.
     * @return A mock User instance.
     */
    private User createUser() {
        User user = new User();
        user.setEmailAddress("Naz@gmail.com");
        user.setPassword(passwordEncoder.encode("naztarr$"));
        return user;
    }

    @Test
    void testGetAllPatrons_Success() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setRole(Role.PATRON);
        User user1 = new User();
        user1.setRole(Role.PATRON);
        User user2 = new User();
        user2.setRole(Role.ADMIN);
        User user3 = new User();
        user3.setRole(Role.PATRON);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user3);
        Page<User> page = new PageImpl<>(users);

        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findAllPatrons(pageable)).thenReturn(page);
        ResponseEntity<ApiResponse<List<UserData>>> response = patronService.getAllPatrons(0, 10);

        assertNotNull(response);
        assertNotNull(response.getBody().getData());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("All patrons successfully fetched", response.getBody().getMessage());
        assertEquals(3, response.getBody().getData().size());


    }

    @Test
    void testGetAllPatrons_UserNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setRole(Role.PATRON);
        User user1 = new User();
        user1.setRole(Role.PATRON);
        User user2 = new User();
        user2.setRole(Role.ADMIN);
        User user3 = new User();
        user3.setRole(Role.PATRON);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user3);
        Page<User> page = new PageImpl<>(users);


        when(userRepository.findAllPatrons(pageable)).thenReturn(page);

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.getAllPatrons(0, 1));
        assertEquals("User not found", exception.getMessage());


    }

    @Test
    void testViewPatronDetail_Success() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        ResponseEntity<ApiResponse<PatronDetail>> response = patronService.viewPatronDetail(mockUser1.getId());

        assertNotNull(response);
        assertNotNull(response.getBody().getData());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("patron details successfully fetched", response.getBody().getMessage());


    }

    @Test
    void testViewPatronDetail_Unauthorized() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.PATRON);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.viewPatronDetail(mockUser1.getId()));
        assertEquals("You are not authorized to view this information", exception.getMessage());


    }

    @Test
    void updatePatronDetail_Success() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.PATRON);

        UserRequest userRequest = new UserRequest("Ebuka", "Okafor",
                "testing121@gmail.com", "08123484382");

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        ResponseEntity<ApiResponse<String>> response = patronService.updatePatronDetail(mockUser.getId(), userRequest);

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Your details are successfully updated", response.getBody().getMessage());

    }

    @Test
    void updatePatronDetail_Unauthorized() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.ADMIN);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        UserRequest userRequest = new UserRequest("Ebuka", "Okafor",
                "testing121@gmail.com", "08123484382");

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.updatePatronDetail(mockUser1.getId(), userRequest));
        assertEquals("You are not authorized to do this", exception.getMessage());

    }

    @Test
    void updatePatronDetail_UserNotFound() {
        String email = "Nz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.ADMIN);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        UserRequest userRequest = new UserRequest("Ebuka", "Okafor",
                "testing121@gmail.com", "08123484382");
        User mockedUser = mock(User.class);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.updatePatronDetail(mockUser1.getId(), userRequest));
        assertEquals("User not found", exception.getMessage());

    }

    @Test
    void removePatron_Success() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.PATRON);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        ResponseEntity<ApiResponse<String>> response = patronService.removePatron(mockUser.getId());

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("You have been removed", response.getBody().getMessage());

    }

    @Test
    void removePatron_UserNotFound() {
        String email = "Nz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.ADMIN);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        User mockedUser = mock(User.class);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.removePatron(mockUser1.getId()));
        assertEquals("User not found", exception.getMessage());

    }

    @Test
    void removePatron_Unauthorized() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.PATRON);

        User mockUser1 = createUser();
        mockUser1.setEmailAddress("test101@gmail.com");
        mockUser1.setId(UUID.randomUUID());
        mockUser1.setRole(Role.PATRON);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> patronService.removePatron(mockUser1.getId()));
        assertEquals("You are not authorized to do this", exception.getMessage());

    }
}