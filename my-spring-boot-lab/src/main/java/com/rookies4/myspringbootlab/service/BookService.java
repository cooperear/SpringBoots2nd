package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        bookRepository.findByIsbn(request.getIsbn()).ifPresent(book -> {
            throw new BusinessException("Book with ISBN " + request.getIsbn() + " already exists.");
        });

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        Book savedBook = bookRepository.save(book);
        return toBookResponse(savedBook);
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book with id " + id + " not found."));
        return toBookResponse(book);
    }

    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(this::toBookResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book with id " + id + " not found."));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        Book updatedBook = bookRepository.save(book);
        return toBookResponse(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book with id " + id + " not found."));
        bookRepository.delete(book);
    }

    private BookDTO.BookResponse toBookResponse(Book book) {
        return BookDTO.BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishDate(book.getPublishDate())
                .build();
    }
}
