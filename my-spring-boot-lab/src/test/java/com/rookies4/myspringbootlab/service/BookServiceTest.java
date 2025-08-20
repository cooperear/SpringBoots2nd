package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_success() {
        // given
        BookDTO.BookCreateRequest request = new BookDTO.BookCreateRequest("title", "author", "isbn", 10.0, LocalDate.now());
        Book book = new Book(1L, "title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());
        when(bookRepository.save(any())).thenReturn(book);

        // when
        BookDTO.BookResponse response = bookService.createBook(request);

        // then
        assertNotNull(response);
        assertEquals("title", response.getTitle());
    }

    @Test
    void createBook_fail_isbnExists() {
        // given
        BookDTO.BookCreateRequest request = new BookDTO.BookCreateRequest("title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findByIsbn(any())).thenReturn(Optional.of(new Book()));

        // when & then
        assertThrows(BusinessException.class, () -> bookService.createBook(request));
    }

    @Test
    void getBookById_success() {
        // given
        Book book = new Book(1L, "title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // when
        BookDTO.BookResponse response = bookService.getBookById(1L);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getBookById_fail_notFound() {
        // given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void getBooksByAuthor_success() {
        // given
        Book book = new Book(1L, "title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findByAuthor("author")).thenReturn(Collections.singletonList(book));

        // when
        List<BookDTO.BookResponse> responses = bookService.getBooksByAuthor("author");

        // then
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void updateBook_success() {
        // given
        BookDTO.BookUpdateRequest request = new BookDTO.BookUpdateRequest("new title", "new author", 20.0, LocalDate.now());
        Book book = new Book(1L, "title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        // when
        BookDTO.BookResponse response = bookService.updateBook(1L, request);

        // then
        assertNotNull(response);
        assertEquals("new title", response.getTitle());
    }

    @Test
    void updateBook_fail_notFound() {
        // given
        BookDTO.BookUpdateRequest request = new BookDTO.BookUpdateRequest("new title", "new author", 20.0, LocalDate.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> bookService.updateBook(1L, request));
    }

    @Test
    void deleteBook_success() {
        // given
        Book book = new Book(1L, "title", "author", "isbn", 10.0, LocalDate.now());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // when & then
        assertDoesNotThrow(() -> bookService.deleteBook(1L));
    }

    @Test
    void deleteBook_fail_notFound() {
        // given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> bookService.deleteBook(1L));
    }
}
