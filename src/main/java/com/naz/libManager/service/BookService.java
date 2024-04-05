package com.naz.libManager.service;

import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.BookData;
import com.naz.libManager.payload.BookDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BookService {

    ResponseEntity<ApiResponse<String>> addBook(BookRequest bookRequest);
    ResponseEntity<ApiResponse<List<BookData>>> getAllBooks(Integer page, Integer size);
    ResponseEntity<ApiResponse<BookDetail>> viewBookDetail(UUID bookId);
    ResponseEntity<ApiResponse<String>> updateBookDetail(UUID bookId, BookRequest bookRequest);
    ResponseEntity<ApiResponse<String>> removeBook(UUID bookId);
}
