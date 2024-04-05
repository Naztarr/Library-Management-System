package com.naz.libManager.controller;

import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.BookData;
import com.naz.libManager.payload.BookDetail;
import com.naz.libManager.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Books",
        description = "REST APIs allowing a/an librarian/admin to manage books in LibManager"
)
public class BookController {
    private final BookService bookService;

    @PostMapping("/books")
    @Operation(summary = "create book",
            description = "Allows an admin to add a new book to the library")
    public ResponseEntity<ApiResponse<String>> createBook(@RequestBody BookRequest bookRequest){
        return ResponseEntity.ok(bookService.addBook(bookRequest).getBody());
    }

    @GetMapping("/books")
    @Operation(summary = "get books",
            description = "retrieves a list of all books available")
    public ResponseEntity<ApiResponse<List<BookData>>> getBooks(@RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(bookService.getAllBooks(page, size).getBody());
    }

    @GetMapping("/books/{id}")
    @Operation(summary = "get book details",
            description = "retrieves the details of a particular book")
    public ResponseEntity<ApiResponse<BookDetail>> getBookDetail(@PathVariable UUID id){
        return ResponseEntity.ok(bookService.viewBookDetail(id).getBody());
    }

    @PutMapping("/books/{id}")
    @Operation(summary = "update book details",
            description = "Allows an admin to edit and update the details of a particular book by its ID")
    public ResponseEntity<ApiResponse<String>> updateBookDetail(@PathVariable UUID id,
                                                                @RequestBody BookRequest bookRequest){

        return ResponseEntity.ok(bookService.updateBookDetail(id, bookRequest).getBody());
    }

    @DeleteMapping("/books/{id}")
    @Operation(summary = "remove book",
            description = "Allows an admin to remove a particular book by its ID")
    public ResponseEntity<ApiResponse<String>> removeBook(@PathVariable UUID id){
        return ResponseEntity.ok(bookService.removeBook(id).getBody());
    }
}
