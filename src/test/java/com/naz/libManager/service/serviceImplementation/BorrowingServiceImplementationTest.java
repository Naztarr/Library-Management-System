package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.BookRecord;
import com.naz.libManager.entity.User;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.repository.BookRepository;
import com.naz.libManager.repository.RecordRepository;
import com.naz.libManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BorrowingServiceImplementationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RecordRepository recordRepository;
    @InjectMocks
    private BorrowingServiceImplementation borrowingService;

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
    void borrowBook() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<String>> response = borrowingService.borrowBook(book.getId(),
                mockUser.getId());

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(String.format("You have now borrowed '%s' by '%s'",
                book.getTitle(), book.getAuthor()), response.getBody().getMessage());
    }

    @Test
    void borrowBook_UserNotFound() {
        String email = "Nz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.borrowBook(book.getId(), mockUser.getId()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void borrowBook_BookDoesNotExist() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());

        UUID id = UUID.randomUUID();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.borrowBook(id, mockUser.getId()));
        assertEquals("Book does not exist", exception.getMessage());
    }

    @Test
    void borrowBook_BookNotAvailable() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(false);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.borrowBook(book.getId(), mockUser.getId()));
        assertEquals("This book is not available", exception.getMessage());
    }

    @Test
    void borrowBook_Unauthorized() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());

        User patron = new User();
        patron.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(patron.getId())).thenReturn(Optional.of(patron));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.borrowBook(book.getId(), patron.getId()));
        assertEquals("You are not authorized to do this", exception.getMessage());
    }

    @Test
    void returnBook() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);
        book.setBorrower(mockUser);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBook(book);
        bookRecord.setReturned(false);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(recordRepository.findByBook(book)).thenReturn(bookRecord);

        ResponseEntity<ApiResponse<String>> response = borrowingService.returnBook(book.getId(),
                mockUser.getId());

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(String.format("You have successfully returned '%s' by '%s'",
                book.getTitle(), book.getAuthor()), response.getBody().getMessage());
    }

    @Test
    void returnBook_UserNotFound() {
        String email = "Nz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);
        book.setBorrower(mockUser);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBook(book);
        bookRecord.setReturned(false);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(recordRepository.findByBook(book)).thenReturn(bookRecord);

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.borrowBook(book.getId(), mockUser.getId()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void returnBook_BookDoesNotExist() {
        String email = "Naz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        UUID id = UUID.randomUUID();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.returnBook(id, mockUser.getId()));
        assertEquals("Book does not exist", exception.getMessage());
    }

    @Test
    void returnBook_BorrowRecordDoesNotExist() {
        String email = "Naz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(recordRepository.findByBook(book)).thenReturn(null);

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.returnBook(book.getId(), mockUser.getId()));
        assertEquals("A borrow record does not exist for this book", exception.getMessage());
    }

    @Test
    void returnBook_BookReturned() {
        String email = "Naz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBook(book);
        bookRecord.setReturned(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(recordRepository.findByBook(book)).thenReturn(null);

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.returnBook(book.getId(), mockUser.getId()));
        assertEquals("A borrow record does not exist for this book", exception.getMessage());
    }

    @Test
    void returnBook_Unauthorized() {
        String email = "Naz@gmail.com";
        User mockUser = mock(User.class);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(false);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBook(book);
        bookRecord.setReturned(false);

        User patron = new User();
        patron.setId(UUID.randomUUID());

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(patron.getId())).thenReturn(Optional.of(patron));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(recordRepository.findByBook(book)).thenReturn(bookRecord);

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> borrowingService.returnBook(book.getId(), patron.getId()));
        assertEquals("You are not authorized to do this", exception.getMessage());
    }
}