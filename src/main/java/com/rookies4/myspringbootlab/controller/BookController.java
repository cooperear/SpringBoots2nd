package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import com.rookies4.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository; // Keep for other methods if they are not in service yet

    // 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        // This method in service seems to have a bug (recursive call), let's fix it later if needed.
        // For now, let's use the repository directly as it was before to not break functionality.
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new BookDTO.BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(), book.getPublishDate()));
    }

    // 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(@Valid @RequestBody BookDTO.BookCreateRequest request) {
        return new ResponseEntity<>(bookService.createBook(request), HttpStatus.CREATED);
    }

    // 도서 정보 수정 (PUT and PATCH)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<BookDTO.BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.BookUpdateRequest request) {
        BookDTO.BookResponse updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    // 저자명으로 도서 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }
}
