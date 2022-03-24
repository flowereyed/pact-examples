package com.vertex.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String id;
    private String title;
    private String author;
    private String year;

    private String userId;
}
