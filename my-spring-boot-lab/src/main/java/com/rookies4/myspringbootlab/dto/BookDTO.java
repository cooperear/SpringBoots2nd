package com.rookies4.myspringbootlab.dto;

import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import jakarta.validation.Valid;
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
    public static class Request {
        @NotBlank(message = "Title is mandatory")
        private String title;

        @NotBlank(message = "Author is mandatory")
        private String author;

        @NotBlank(message = "ISBN is mandatory")
        private String isbn;

        private Integer price;

        private LocalDate publishDate;

        @Valid
        @NotNull
        private BookDetailDTO detailRequest;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = null;
            if (book.getBookDetail() != null) {
                detailResponse = BookDetailResponse.fromEntity(book.getBookDetail());
            }

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public static BookDetailResponse fromEntity(BookDetail bookDetail) {
            return BookDetailResponse.builder()
                    .id(bookDetail.getId())
                    .description(bookDetail.getDescription())
                    .language(bookDetail.getLanguage())
                    .pageCount(bookDetail.getPageCount())
                    .publisher(bookDetail.getPublisher())
                    .coverImageUrl(bookDetail.getCoverImageUrl())
                    .edition(bookDetail.getEdition())
                    .build();
        }
    }
}
