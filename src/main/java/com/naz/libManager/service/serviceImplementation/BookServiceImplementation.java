package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.User;
import com.naz.libManager.enums.Role;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.mapper.BookMapper;
import com.naz.libManager.payload.*;
import com.naz.libManager.repository.BookRepository;
import com.naz.libManager.repository.UserRepository;
import com.naz.libManager.service.BookService;
import com.naz.libManager.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * Implementation of BookService providing operations related to books.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "book")
public class BookServiceImplementation implements BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * Adds a new book to the library.
     *
     * @param bookRequest The request containing book details
     * @return ResponseEntity containing ApiResponse confirming the book addition
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> addBook(BookRequest bookRequest) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        if(user.getRole() == Role.ADMIN){
            bookRepository.save(BookMapper.mapBookRequestToBook(new Book(), bookRequest));
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity
                .ok(new ApiResponse<>(String.format("'%s' by %s has been added successfully", bookRequest.title(), bookRequest.author()),
                        HttpStatus.OK));
    }

    /**
     * Retrieves a paginated list of all available books.
     *
     * @param pageNumber The page number (starts from 0)
     * @param pageSize   The size of each page
     * @return ResponseEntity containing ApiResponse with a list of BookData representing available books
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public ResponseEntity<ApiResponse<List<BookData>>> getAllBooks(Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber != null && pageNumber >= 0 ? pageNumber : 0;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));

        Page<Book> books = bookRepository.findAllByAvailableTrue(pageable);
        return ResponseEntity.ok(new ApiResponse<>(
                books.stream()
                        .map(book -> BookMapper.mapBookToBookData(book, new BookData()))
                        .collect(Collectors.toList()), "Available books successfully fetched"
        ));
    }

    /**
     * Retrieves detailed information about a specific book.
     *
     * @param bookId The UUID of the book
     * @return ResponseEntity containing ApiResponse with the BookDetail
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#bookId")
    public ResponseEntity<ApiResponse<BookDetail>> viewBookDetail(UUID bookId) {
        userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibManagerException("book not found"));

        return ResponseEntity.ok(new ApiResponse<>(BookMapper.mapBookToBookDetail(book, new BookDetail()),
                "book details successfully fetched"));
    }

    /**
     * Updates the details of a specific book.
     *
     * @param bookId      The UUID of the book
     * @param bookRequest The BookRequest containing updated details
     * @return ResponseEntity containing ApiResponse confirming the update
     */
    @Override
    @Transactional
    @CachePut(key = "#bookId")
    public ResponseEntity<ApiResponse<String>> updateBookDetail(UUID bookId, BookRequest bookRequest) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibManagerException("book not found"));
        if(user.getRole() == Role.ADMIN){
            bookRepository.save(BookMapper.mapBookRequestToBook(book, bookRequest));
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity
                .ok(new ApiResponse<>("Book information is successfully updated", HttpStatus.OK));
    }

    /**
     * Removes a specific book from the library.
     *
     * @param bookId The UUID of the book to be removed
     * @return ResponseEntity containing ApiResponse confirming the removal
     */
    @Override
    @Transactional
    @CacheEvict(key = "#bookId")
    public ResponseEntity<ApiResponse<String>> removeBook(UUID bookId) {
        User user =userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibManagerException("book not found"));
        if(user.getRole() == Role.ADMIN){
            bookRepository.delete(book);
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity.ok(new ApiResponse<>(String.format("'%s' by %s has been removed", book.getTitle(), book.getAuthor()),
                HttpStatus.OK));    }
}
