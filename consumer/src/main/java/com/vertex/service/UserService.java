package com.vertex.service;

import com.github.javafaker.Faker;
import com.vertex.data.User;
import com.vertex.endpoint.BookEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final int USER_COUNT = 10;
    private final Faker faker = new Faker();
    private final BookEndpoint endpoint;

    public User getUser(String id) {
        return User.builder()
                .id(id)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.bothify("????##@gmail.com"))
                .books(endpoint.getBooksByUserId(id))
                .build();
    }

    public List<User> getUsers() {
        return Stream.iterate(0, i -> i + 1)
                .limit(USER_COUNT)
                .map((i) -> getUser(UUID.randomUUID().toString()))
                .collect(Collectors.toList());
    }
}
