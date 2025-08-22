package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    // 전체 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO.Response> responses = books.stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(BookDTO.Response.fromEntity(book));
    }

    // 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        return new ResponseEntity<>(bookService.createBook(request), HttpStatus.CREATED);
    }

    // 도서 정보 수정 (PUT & PATCH)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id, @RequestBody BookDTO.Request request, HttpServletRequest httpRequest) {
        BookDTO.Response updatedBook = bookService.updateOrPatchBook(id, request, httpRequest.getMethod());
        return ResponseEntity.ok(updatedBook);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(BookDTO.Response.fromEntity(book));
    }

    // 저자명으로 도서 조회
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@PathVariable String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        List<BookDTO.Response> responses = books.stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // 제목으로 도서 조회
    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@PathVariable String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookDTO.Response> responses = books.stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ISBN 존재 여부 확인
    @GetMapping("/isbn/{isbn}/exists")
    public boolean existsByIsbn(@PathVariable String isbn){
        return bookRepository.existsByIsbn(isbn);
    }


}