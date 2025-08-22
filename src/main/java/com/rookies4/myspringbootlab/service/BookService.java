package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.entity.BookDetail;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.exception.advice.ErrorCode;
import com.rookies4.myspringbootlab.repository.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final Validator validator;

    public BookDTO.Response createBook(BookDTO.Request request) {

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }


        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO detailDTO = request.getDetailRequest();
            BookDetail bookDetail = BookDetail.builder()
                    .description(detailDTO.getDescription())
                    .language(detailDTO.getLanguage())
                    .pageCount(detailDTO.getPageCount())
                    .publisher(detailDTO.getPublisher())
                    .coverImageUrl(detailDTO.getCoverImageUrl())
                    .edition(detailDTO.getEdition())
                    .build();
            book.setBookDetail(bookDetail);
        }

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        // For PUT, manually validate the request body
        Set<ConstraintViolation<BookDTO.Request>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Book existBook = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("책 없음", HttpStatus.NOT_FOUND));

        // Full update
        existBook.setTitle(request.getTitle());
        existBook.setAuthor(request.getAuthor());
        existBook.setPrice(request.getPrice());
        existBook.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDetail bookDetail = existBook.getBookDetail();
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                existBook.setBookDetail(bookDetail);
            }
            BookDTO.BookDetailDTO detailDTO = request.getDetailRequest();
            bookDetail.setDescription(detailDTO.getDescription());
            bookDetail.setLanguage(detailDTO.getLanguage());
            bookDetail.setPageCount(detailDTO.getPageCount());
            bookDetail.setPublisher(detailDTO.getPublisher());
            bookDetail.setCoverImageUrl(detailDTO.getCoverImageUrl());
            bookDetail.setEdition(detailDTO.getEdition());
        }

        if (!existBook.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book updatedBook = bookRepository.save(existBook);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    public BookDTO.Response patchBook(Long id, BookDTO.Request request) {
        Book existBook = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("책 없음", HttpStatus.NOT_FOUND));


        // Partial update
        if (StringUtils.hasText(request.getTitle())) {
            existBook.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getAuthor())) {
            existBook.setAuthor(request.getAuthor());
        }
        if (StringUtils.hasText(request.getIsbn())) {
            existBook.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        if (request.getDetailRequest() != null) {
            BookDetail bookDetail = existBook.getBookDetail();
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                existBook.setBookDetail(bookDetail);
            }
            BookDTO.BookDetailDTO detailDTO = request.getDetailRequest();
            if (StringUtils.hasText(detailDTO.getDescription())) {
                bookDetail.setDescription(detailDTO.getDescription());
            }
            if (StringUtils.hasText(detailDTO.getLanguage())) {
                bookDetail.setLanguage(detailDTO.getLanguage());
            }
            if (detailDTO.getPageCount() != null) {
                bookDetail.setPageCount(detailDTO.getPageCount());
            }
            if (StringUtils.hasText(detailDTO.getPublisher())) {
                bookDetail.setPublisher(detailDTO.getPublisher());
            }
            if (StringUtils.hasText(detailDTO.getCoverImageUrl())) {
                bookDetail.setCoverImageUrl(detailDTO.getCoverImageUrl());
            }
            if (StringUtils.hasText(detailDTO.getEdition())) {
                bookDetail.setEdition(detailDTO.getEdition());
            }
        }

        if (!existBook.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book patchedBook = bookRepository.save(existBook);
        return BookDTO.Response.fromEntity(patchedBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("책없음", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }

    public BookDTO.Response patchBookDetail(Long bookId, BookDTO.BookDetailDTO detailRequest) {
        Book existBook = bookRepository.findByIdWithBookDetail(bookId)
                .orElseThrow(() -> new BusinessException("Book not found", HttpStatus.NOT_FOUND));

        BookDetail bookDetail = existBook.getBookDetail();
        if (bookDetail == null) {
            throw new BusinessException("BookDetail not found, cannot patch", HttpStatus.NOT_FOUND);
        }

        if (StringUtils.hasText(detailRequest.getDescription())) {
            bookDetail.setDescription(detailRequest.getDescription());
        }
        if (StringUtils.hasText(detailRequest.getLanguage())) {
            bookDetail.setLanguage(detailRequest.getLanguage());
        }
        if (detailRequest.getPageCount() != null) {
            bookDetail.setPageCount(detailRequest.getPageCount());
        }
        if (StringUtils.hasText(detailRequest.getPublisher())) {
            bookDetail.setPublisher(detailRequest.getPublisher());
        }
        if (StringUtils.hasText(detailRequest.getCoverImageUrl())) {
            bookDetail.setCoverImageUrl(detailRequest.getCoverImageUrl());
        }
        if (StringUtils.hasText(detailRequest.getEdition())) {
            bookDetail.setEdition(detailRequest.getEdition());
        }

        Book patchedBook = bookRepository.save(existBook);
        return BookDTO.Response.fromEntity(patchedBook);
    }
}