package com.vertex.controller;

import com.vertex.data.Book;
import com.vertex.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> getBooksByUserId(@RequestParam("userId") String userId) {
        return bookService.getBooksByUserId(userId);
    }
}
