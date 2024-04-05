package com.naz.libManager.service;

import com.naz.libManager.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface BorrowingService {

    ResponseEntity<ApiResponse<String>> borrowBook(UUID bookId, UUID patronId);
    ResponseEntity<ApiResponse<String>> returnBook(UUID bookId, UUID patronId);
}
