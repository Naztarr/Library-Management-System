package com.naz.libManager.payload;

import com.naz.libManager.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatronDetail {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private List<Book> booksBorrowed;
}
