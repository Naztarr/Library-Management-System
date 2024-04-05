package com.naz.libManager.payload;

import com.naz.libManager.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetail {
    private String title;
    private String author;
    private Long publicationYear;
    private String isbn;
    private Boolean available;
    private User borrower;
}
