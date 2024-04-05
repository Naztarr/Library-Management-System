package com.naz.libManager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "books")
@Table(name = "books")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity{
    @Column(unique = true)
    private String title;

    private String author;

    private Long publicationYear;

    private String isbn;

    private Boolean available = true;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "book_user_fkey")
    )
    private User borrower = null;
}
