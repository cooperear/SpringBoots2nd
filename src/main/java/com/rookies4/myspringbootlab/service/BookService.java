package com.rookies4.myspringbootlab.service;

import com.rookies4.myspringbootlab.controller.dto.BookDTO;

public class BookService {
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        //변경이 필요한 필드만 업데이트
        BookDTO.BookUpdateRequest existBook =  request.;
        if(request.getTitle() != null) {

            existBook.setTitle(request.getTitle());
        }
    }

}
