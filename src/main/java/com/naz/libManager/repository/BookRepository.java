package com.naz.libManager.repository;

import com.naz.libManager.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
//    @Query("SELECT b FROM books b WHERE b.available = true GROUP BY b.title")
    Page<Book> findAllByAvailableTrue(Pageable pageable);
    @Query("SELECT b FROM books b WHERE (b.author LIKE %?1% OR b.title LIKE %?2%) AND b.available = true")
    Page<Book> findByAuthorOrTitleAndAvailableTrue(String author, String title, Pageable pageable);
}


