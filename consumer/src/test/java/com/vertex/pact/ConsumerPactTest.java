package com.vertex.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.vertex.data.Book;
import com.vertex.endpoint.BookEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonArrayMinLike;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerPactTest {

    private static final String UUID_REGEX = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";
    private static final String INVALID_USER_ID = "ca27d693-56a0-4319-8b48-2564f9307fca";

    @Pact(provider = "bookService", consumer = "bookEndpoint")
    public RequestResponsePact getBooksByUserId(PactDslWithProvider builder) {
        return builder.given("books exists")
                .uponReceiving("Get books by user id")
                .path("/books")
                .matchQuery("userId", UUID_REGEX, "24daf846-5268-4835-b4c5-03361cc55e8f")
                .method(HttpMethod.GET.name())
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(headers())
                .body(newJsonArrayMinLike(1, bookArray ->
                        bookArray.object(bookObject -> {
                            bookObject.uuid("id",
                                    UUID.fromString("24daf846-5268-4835-b4c5-03361cc55e8f"));
                            bookObject.stringType("title", "The Green Bay Tree");
                            bookObject.stringType("author", "Antwan Price");
                            bookObject.stringType("year", "1906");
                            bookObject.uuid("userId",
                                    UUID.fromString("24daf846-5268-4835-b4c5-03361cc55e8f"));
                        }))
                        .build())
                .toPact();
    }

    @Pact(provider = "bookService", consumer = "bookEndpoint")
    public RequestResponsePact getBooksByNonExistentUserId(PactDslWithProvider builder) {
        return builder.given("Books for user that does not exist")
                .uponReceiving("Get books for user that does not exist")
                .path("/books")
                .matchQuery("userId", INVALID_USER_ID)
                .method(HttpMethod.GET.name())
                .willRespondWith()
                .status(HttpStatus.NOT_FOUND.value())
                .headers(headers())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getBooksByUserId")
    void testGetBooksByUserId(MockServer mockServer) {
        BookEndpoint endpoint = getEndpoint(mockServer);
        List<Book> books = endpoint.getBooksByUserId(UUID.randomUUID().toString());

        assertThat(books).isNotEmpty();
    }

    @Test
    @PactTestFor(pactMethod = "getBooksByNonExistentUserId")
    void testGetBooksByNonExistedUserId(MockServer mockServer) {
        BookEndpoint endpoint = getEndpoint(mockServer);

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class,
                () -> endpoint.getBooksByUserId(INVALID_USER_ID));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }


    private BookEndpoint getEndpoint(MockServer mockServer) {
        BookEndpoint bookEndpoint = new BookEndpoint();
        ReflectionTestUtils.setField(bookEndpoint, "baseUrl", mockServer.getUrl());
        return bookEndpoint;
    }

    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
