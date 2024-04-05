package com.naz.libManager.controller;

import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Borrowing endpoint",
        description = "REST APIs for borrowing and returning of books by patrons"
)
public class BorrowController {
    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/{patronId}")
    @Operation(summary = "borrow book",
            description = "Allows a patron to borrow a book")
    public ResponseEntity<ApiResponse<String>> borrowBook(@PathVariable UUID bookId, @PathVariable UUID patronId){
        return ResponseEntity.ok(borrowingService.borrowBook(bookId, patronId).getBody());
    }

    @PutMapping("/return/{bookId}/{patronId}")
    @Operation(summary = "return book",
            description = "returns a borrowed book")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable UUID bookId, @PathVariable UUID patronId){
        return ResponseEntity.ok(borrowingService.returnBook(bookId, patronId).getBody());
    }
}
