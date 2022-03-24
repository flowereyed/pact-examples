package com.vertex.endpoint;

import com.vertex.data.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookEndpoint {

    @Value("${book-service.url}")
    private String baseUrl;
    private final RestTemplate template = new RestTemplate();

    public List<Book> getBooksByUserId(String userId) {
        return template.exchange(baseUrl + "/books?userId=" + userId,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
                }).getBody();
    }
}
