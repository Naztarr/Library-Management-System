package com.naz.libManager.dto;

import jakarta.validation.constraints.*;


public record BookRequest(@NotBlank(message = "Title is required")
                          String title,

                          @NotBlank(message = "Author is required")
                          String author,

                          @NotNull(message = "Publication year is required")
                          Long publicationYear,

                          @NotBlank(message = "ISBN is required")
                          @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
                          @Pattern(regexp = "\\d{10,13}", message = "ISBN must contain only digits")
                          String isbn) {
}
