package com.rookies4.myspringbootlab.controller;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;
import com.rookies4.myspringbootlab.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookDetailController {

    private final BookService bookService;

    @PatchMapping("/{bookId}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(
            @PathVariable Long bookId,
            @RequestBody BookDTO.BookDetailDTO detailRequest) {
        BookDTO.Response response = bookService.patchBookDetail(bookId, detailRequest);
        return ResponseEntity.ok(response);
    }
}
