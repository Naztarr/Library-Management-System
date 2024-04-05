package com.naz.libManager.controller;

import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.User;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.service.BookService;
import com.naz.libManager.service.serviceImplementation.BorrowingServiceImplementation;
import com.naz.libManager.service.serviceImplementation.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BorrowController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class BorrowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BorrowingServiceImplementation borrowingService;
    @MockBean
    BookService bookService;
    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;
    @InjectMocks
    BorrowController borrowController;

    @Test
    void borrowBook() throws Exception {
        UUID bookId = UUID.randomUUID();
        UUID patronId = UUID.randomUUID();


        ApiResponse<String> apiResponse = new ApiResponse<>("Book borrowed successfully", HttpStatus.OK);


        when(borrowingService.borrowBook(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/{bookId}/{patronId}", bookId, patronId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book borrowed successfully"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(borrowingService).borrowBook(any(UUID.class), any(UUID.class));
    }

    @Test
    void returnBook() throws Exception {
        Book book = new Book();
        book.setId(UUID.randomUUID());

        User patron = new User();
        patron.setId(UUID.randomUUID());

        ApiResponse<String> apiResponse = new ApiResponse<>("Book returned successfully", HttpStatus.OK);

        when(borrowingService.returnBook(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/{bookId}/{patronId}", book.getId(), patron.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Book returned successfully"));

        verify(borrowingService).returnBook(any(UUID.class), any(UUID.class));
    }


}
