package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.entity.Book;
import com.rookies4.myspringbootlab.exception.BusinessException;
import com.rookies4.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class BookService {
    private final BookRepository bookRepository;

    public List<BookDTO.BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO.BookResponse> bookResponses = new java.util.ArrayList<>();
        for (Book book : books) {
            bookResponses.add(new BookDTO.BookResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPrice(),
                    book.getPublishDate()
            ));
        }
        return bookResponses;
    }

    public BookDTO.BookResponse getBookById(Long id) {

        BookDTO.BookResponse bookById = getBookById(id);


        return bookById;

    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        BookDTO.BookResponse bookByIsbn = getBookByIsbn(isbn);
        return bookByIsbn;
    }

    public BookDTO.BookResponse getBookByAuthor(String author) {

        BookDTO.BookResponse bookByAuthor = getBookByAuthor(author);
        return bookByAuthor;

    }

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        bookRepository.findByIsbn(request.getIsbn()).ifPresent(entity -> {
            throw new BusinessException("이미 등록된 isbn 입니다.", HttpStatus.CONFLICT);
        });
        Book entity = request.toEntity();
        Book savedEntity = bookRepository.save(entity);

//        return new BookDTO.BookResponse(savedEntity); 더티코딩 그는 신이야! 안먹히네요 ㅎㅎ;;;;

        return new BookDTO.BookResponse(
                savedEntity.getId(),
                savedEntity.getTitle(),
                savedEntity.getAuthor(),
                savedEntity.getIsbn(),
                savedEntity.getPrice(),
                savedEntity.getPublishDate()
        );

    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not find", HttpStatus.NOT_FOUND));

        // 유지보수 최적화(더티함)
        
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice().intValue());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        Book updatedBook = bookRepository.save(existBook);

        return new BookDTO.BookResponse(
                updatedBook.getId(),
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getIsbn(),
                updatedBook.getPrice(),
                updatedBook.getPublishDate()
        );

    }

    @Transactional
    public void deleteBook(Long id) {
        Book byId = bookRepository.getById(id);
        bookRepository.delete(byId);
    }


}
