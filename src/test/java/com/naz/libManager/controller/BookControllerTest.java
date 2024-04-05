package com.naz.libManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.BookData;
import com.naz.libManager.payload.BookDetail;
import com.naz.libManager.service.BookService;
import com.naz.libManager.service.serviceImplementation.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createBook() throws Exception {
        BookRequest bookRequest = new BookRequest("Title", "Author", 2024L, "0983637484246");
        ApiResponse<String> apiResponse = new ApiResponse<>("Book created successfully", HttpStatus.OK);

        when(bookService.addBook(any(BookRequest.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book created successfully"));
        verify(bookService).addBook(any(BookRequest.class));
    }

    @Test
    void getBooks() throws Exception {
        List<BookData> bookList = new ArrayList<>();
        bookList.add(new BookData());
        bookList.add(new BookData());

        ApiResponse<List<BookData>> apiResponse = new ApiResponse<>(bookList, "All books fetched successfully");

        when(bookService.getAllBooks(anyInt(), anyInt())).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All books fetched successfully"));

        verify(bookService).getAllBooks(anyInt(), anyInt());
    }

    @Test
    void getBookDetail() throws Exception {
        UUID id = UUID.randomUUID();
        BookDetail bookDetail = new BookDetail();

        ApiResponse<BookDetail> apiResponse = new ApiResponse<>(bookDetail, "Book details fetched successfully");

        when(bookService.viewBookDetail(any(UUID.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book details fetched successfully"));

        verify(bookService).viewBookDetail(any(UUID.class));
    }

    @Test
    void updateBookDetail() throws Exception {
        UUID id = UUID.randomUUID();
        BookRequest bookRequest = new BookRequest("Title", "Author", 2024L, "0364738465");

        ApiResponse<String> apiResponse = new ApiResponse<>("Book details updated successfully", HttpStatus.OK);

        when(bookService.updateBookDetail(any(UUID.class), any(BookRequest.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Book details updated successfully"));

        verify(bookService).updateBookDetail(any(UUID.class), any(BookRequest.class));
    }

    @Test
    void removeBook() throws Exception {
        UUID id = UUID.randomUUID();
        ApiResponse<String> apiResponse = new ApiResponse<>("Book removed successfully", HttpStatus.OK);

        when(bookService.removeBook(any(UUID.class))).thenReturn(ResponseEntity.ok(apiResponse));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Book removed successfully"));

        verify(bookService).removeBook(any(UUID.class));
    }
}