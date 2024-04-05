package com.naz.libManager.repository;


import com.naz.libManager.entity.Book;
import com.naz.libManager.entity.BookRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<BookRecord, UUID> {
    BookRecord findByBook(Book book);
}
