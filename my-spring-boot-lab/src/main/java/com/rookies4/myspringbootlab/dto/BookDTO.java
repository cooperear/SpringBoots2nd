package com.rookies4.myspringbootlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank(message = "Title is mandatory")
        private String title;

        @NotBlank(message = "Author is mandatory")
        private String author;

        @NotBlank(message = "ISBN is mandatory")
        private String isbn;

        private Double price;

        private LocalDate publishDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateRequest {
        @NotBlank(message = "Title is mandatory")
        private String title;

        @NotBlank(message = "Author is mandatory")
        private String author;

        private Double price;

        private LocalDate publishDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private double price;
        private LocalDate publishDate;

        public BookResponse(com.rookies4.myspringbootlab.entity.Book book) {
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
            this.price = book.getPrice();
            this.publishDate = book.getPublishDate();
        }
    }
}
