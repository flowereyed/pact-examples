package com.vertex.service;

import com.github.javafaker.Faker;
import com.vertex.data.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookService {

    private static final String INVALID_USER_ID = "ca27d693-56a0-4319-8b48-2564f9307fca";

    private final Faker faker = new Faker();
    private final Random random = new Random();

    public List<Book> getBooksByUserId(String userId) {
        if (userId.equals(INVALID_USER_ID)) {
            throw new RuntimeException("Books do not exist");
        }
        return Stream.iterate(0, i -> i + 1)
                .limit(random.nextInt(2) + 2)
                .map((i) -> getBookById(userId))
                .collect(Collectors.toList());
    }

    private Book getBookById(String userId) {
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .author(faker.book().author())
                .title(faker.book().title())
                .year(String.valueOf(random.nextInt(50) + 1900))
                .userId(userId)
                .build();
    }
}
