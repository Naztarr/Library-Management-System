package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.BookRecord;
import com.naz.libManager.entity.User;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.repository.BookRepository;
import com.naz.libManager.repository.RecordRepository;
import com.naz.libManager.repository.UserRepository;
import com.naz.libManager.service.BorrowingService;
import com.naz.libManager.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of BorrowingService providing operations related to borrowing and returning books.
 */
@Service
@RequiredArgsConstructor
public class BorrowingServiceImplementation implements BorrowingService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RecordRepository recordRepository;

    /**
     * Allows a user to borrow a book.
     *
     * @param bookId   The UUID of the book to be borrowed
     * @param patronId The UUID of the user borrowing the book
     * @return ResponseEntity containing ApiResponse confirming the book has been borrowed
     * @throws LibManagerException if the user or book does not exist, or if the user is not authorized to perform the action
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> borrowBook(UUID bookId, UUID patronId) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        User user1 = userRepository.findById(patronId)
                .orElseThrow(() -> new LibManagerException("User does not exist"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibManagerException("Book does not exist"));
        if(user.equals(user1)){
            if(book.getAvailable()){
                book.setAvailable(false);
                book.setBorrower(user);
                bookRepository.save(book);

                user.getBorrowedBooks().add(book);
                userRepository.save(user);

                BookRecord bookRecord = new BookRecord();
                bookRecord.setBook(book);
                recordRepository.save(bookRecord);
            } else{
                throw new LibManagerException("This book is not available");
            }
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity
                .ok(new ApiResponse<>(String.format("You have now borrowed '%s' by '%s'",
                        book.getTitle(), book.getAuthor()), HttpStatus.OK));
    }

    /**
     * Allows a user to return a borrowed book.
     *
     * @param bookId   The UUID of the book to be returned
     * @param patronId The UUID of the user returning the book
     * @return ResponseEntity containing ApiResponse confirming the book has been returned
     * @throws LibManagerException if the user or book does not exist, or if there is no borrow record for the book,
     *                             or if the user is not authorized to perform the action
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> returnBook(UUID bookId, UUID patronId) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        User user1 = userRepository.findById(patronId)
                .orElseThrow(() -> new LibManagerException("User does not exist"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibManagerException("Book does not exist"));
        BookRecord bookRecord = recordRepository.findByBook(book);
        if(bookRecord != null){
            if(!bookRecord.getReturned()){
                if(user.equals(user1) && user.equals(book.getBorrower())){
                    book.setAvailable(true);
                    book.setBorrower(null);
                    bookRepository.save(book);

                    user.getBorrowedBooks().remove(book);
                    userRepository.save(user);

                    bookRecord.setBook(null);
                    bookRecord.setReturned(true);
                    recordRepository.save(bookRecord);

                } else{
                    throw new LibManagerException("You are not authorized to do this");
                }
            } else{
                throw new LibManagerException("This book has been returned");
            }
        } else{
            throw new LibManagerException("A borrow record does not exist for this book");
        }
        return ResponseEntity
                .ok(new ApiResponse<>(String.format("You have successfully returned '%s' by '%s'",
                        book.getTitle(), book.getAuthor()), HttpStatus.OK));
    }
}
