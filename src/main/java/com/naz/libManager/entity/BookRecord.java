package com.naz.libManager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "book_records")
@Table(name = "book_records")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookRecord{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "record_book_fkey")
    )
    private Book book = null;

    @CreationTimestamp
    @CreatedDate
    @Column(name = "borrowed_at", updatable = false)
    private LocalDateTime borrowedAt;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    private Boolean returned = false;

}
