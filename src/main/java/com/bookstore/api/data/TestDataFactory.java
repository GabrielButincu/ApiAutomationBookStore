package com.bookstore.api.data;

import com.bookstore.api.models.Author;
import com.bookstore.api.models.Book;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataFactory {
    
    private static final Faker faker = new Faker();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    public static Book createRandomBook() {
        return Book.builder()
                .id(generateRandomId())
                .title(faker.book().title())
                .description(faker.lorem().paragraph())
                .pageCount(ThreadLocalRandom.current().nextInt(100, 1000))
                .excerpt(faker.lorem().sentence())
                .publishDate(generateRandomDate())
                .build();
    }
    
    public static Book createBookWithoutId() {
        Book book = createRandomBook();
        book.setId(null);
        return book;
    }
    
    public static Book createInvalidBook() {
        return Book.builder()
                .id(-1)
                .title("")
                .description(null)
                .pageCount(-100)
                .excerpt("")
                .publishDate("invalid-date")
                .build();
    }
    
    public static Author createRandomAuthor() {
        return Author.builder()
                .id(generateRandomId())
                .idBook(generateRandomId())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();
    }
    
    public static Author createAuthorWithoutId() {
        Author author = createRandomAuthor();
        author.setId(null);
        return author;
    }
    
    public static Author createInvalidAuthor() {
        return Author.builder()
                .id(-1)
                .idBook(-1)
                .firstName("")
                .lastName("")
                .build();
    }
    
    private static Integer generateRandomId() {
        return ThreadLocalRandom.current().nextInt(1, 10000);
    }
    
    private static String generateRandomDate() {
        LocalDateTime randomDate = LocalDateTime.now()
                .minusDays(ThreadLocalRandom.current().nextInt(1, 3650));
        return randomDate.format(formatter);
    }
}
