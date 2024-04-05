package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.User;
import com.naz.libManager.enums.Role;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.BookData;
import com.naz.libManager.payload.BookDetail;
import com.naz.libManager.repository.BookRepository;
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
class BookServiceImplementationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private BookServiceImplementation bookService;

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
    void addBook_Success() {
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));

        ResponseEntity<ApiResponse<String>> response = bookService.addBook(bookRequest);

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(String.format("'%s' by %s has been added successfully", bookRequest.title(),
                bookRequest.author()), response.getBody().getMessage());
    }


    @Test
    void addBook_UserNotFound() {
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Nz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);

        User mockedUser = mock(User.class);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.addBook(bookRequest));
        assertEquals("User not found", exception.getMessage());
    }


    @Test
    void addBook_Unauthorized() {
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.PATRON);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.addBook(bookRequest));
        assertEquals("You are not authorized to do this", exception.getMessage());
    }


    @Test
    void getAllBooks_Success() {
        Pageable pageable = PageRequest.of(0, 1);
        Book book = new Book();
        book.setAvailable(true);
        Book book1 = new Book();
        book1.setAvailable(true);
        Book book2 = new Book();
        book2.setAvailable(true);
        Book book3 = new Book();
        book3.setAvailable(true);

        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book1);
        books.add(book2);
        books.add(book3);
        Page<Book> page = new PageImpl<>(books);

        String email = "Naz@gmail.com";
        User mockUser = createUser();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findAllByAvailableTrue(pageable)).thenReturn(page);

        ResponseEntity<ApiResponse<List<BookData>>> response = bookService.getAllBooks(0, 1);

        assertNotNull(response);
        assertNotNull(response.getBody().getData());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Available books successfully fetched", response.getBody().getMessage());
        assertEquals(4, response.getBody().getData().size());
    }


    @Test
    void getAllBooks_UserNotFound() {

        String email = "Nz@gmail.com";
        User mockedUser = mock(User.class);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));

     LibManagerException exception = assertThrows(LibManagerException.class,
             () -> bookService.getAllBooks(0, 1));
     assertEquals("User not found", exception.getMessage());
    }


    @Test
    void viewBookDetail_Success() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<BookDetail>> response = bookService.viewBookDetail(book.getId());

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("book details successfully fetched", response.getBody().getMessage());

    }


    @Test
    void viewBookDetail_UserNotfound() {
        String email = "Nz@gmail.com";
        User mockedUser = mock(User.class);

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.viewBookDetail(book.getId()));
        assertEquals("User not found", exception.getMessage());

    }


    @Test
    void viewBookDetail_BokNotFound() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();

        UUID id = UUID.randomUUID();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.viewBookDetail(id));
        assertEquals("book not found", exception.getMessage());

    }


    @Test
    void updateBookDetail_Success() {
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<String>> response = bookService.updateBookDetail(book.getId(), bookRequest);

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(String.format("Book information is successfully updated", bookRequest.title(),
                bookRequest.author()), response.getBody().getMessage());

    }


    @Test
    void updateBookDetail_UserNotFound() {
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Nz@gmail.com";
        User mockedUser = mock(User.class);

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.updateBookDetail(book.getId(), bookRequest));
        assertEquals("User not found", exception.getMessage());

    }


    @Test
    void updateBookDetail_BookNotFound(){
        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        String email = "Naz@gmail.com";
        User mockUser = createUser();

        UUID id = UUID.randomUUID();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.updateBookDetail(id, bookRequest));
        assertEquals("book not found", exception.getMessage());
    }


    @Test
    void updateBookDetail_Unauthorized(){
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setRole(Role.PATRON);

        BookRequest bookRequest = new BookRequest("Introduction to Java", "Naztarr",
                2024L, "0689232738273");
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.updateBookDetail(book.getId(), bookRequest));
        assertEquals("You are not authorized to do this", exception.getMessage());

    }

    @Test
    void removeBook_Success() {
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.ADMIN);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setTitle("Programming Concepts");
        book.setAuthor("Naz");
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<String>> response = bookService.removeBook(book.getId());

        assertNotNull(response);
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals((String.format("'%s' by %s has been removed", book.getTitle(),
                        book.getAuthor())), response.getBody().getMessage());

    }

    @Test
    void removeBook_UserNotFound() {
        String email = "Nz@gmail.com";
        User mockedUser = mock(User.class);
        mockedUser.setRole(Role.ADMIN);
        mockedUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setTitle("Programming Concepts");
        book.setAuthor("Naz");
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.removeBook(book.getId()));
        assertEquals("User not found", exception.getMessage());

    }

    @Test
    void removeBook_BookNotFound() {
        String email = "Naz@gmail.com";
        User mockedUser = mock(User.class);
        mockedUser.setRole(Role.ADMIN);
        mockedUser.setId(UUID.randomUUID());

        UUID id = UUID.randomUUID();

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockedUser));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.removeBook(id));
        assertEquals("book not found", exception.getMessage());

    }

    @Test
    void removeBook_Unauthorized(){
        String email = "Naz@gmail.com";
        User mockUser = createUser();
        mockUser.setRole(Role.PATRON);
        mockUser.setId(UUID.randomUUID());

        Book book = new Book();
        book.setTitle("Programming Concepts");
        book.setAuthor("Naz");
        book.setId(UUID.randomUUID());
        book.setAvailable(true);

        when(userRepository.findByEmailAddress(email)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibManagerException exception = assertThrows(LibManagerException.class,
                () -> bookService.removeBook(book.getId()));
        assertEquals("You are not authorized to do this", exception.getMessage());
    }
}