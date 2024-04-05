package com.naz.libManager.mapper;

import com.naz.libManager.dto.BookRequest;
import com.naz.libManager.entity.Book;
import com.naz.libManager.payload.BookData;
import com.naz.libManager.payload.BookDetail;

public class BookMapper {
    public static Book mapBookRequestToBook(Book book, BookRequest bookRequest){
        book.setTitle(bookRequest.title());
        book.setAuthor(bookRequest.author());
        book.setPublicationYear(bookRequest.publicationYear());
        book.setIsbn(bookRequest.isbn());
        return book;
    }

    public static BookData mapBookToBookData(Book book, BookData bookData){
        bookData.setTitle(book.getTitle());
        bookData.setAuthor(book.getAuthor());
        return bookData;
    }

    public static BookDetail mapBookToBookDetail(Book book, BookDetail bookdetail){
        bookdetail.setTitle(book.getTitle());
        bookdetail.setAuthor(book.getAuthor());
        bookdetail.setPublicationYear(book.getPublicationYear());
        bookdetail.setIsbn(book.getIsbn());
        bookdetail.setAvailable(book.getAvailable());
        bookdetail.setBorrower(book.getBorrower());
        return bookdetail;
    }
}
