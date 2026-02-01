package com.bookstore.api.clients;

import com.bookstore.api.models.Book;
import com.bookstore.api.specs.RequestSpecs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BooksApiClient extends BaseApiClient {
    
    private static final String BOOKS_ENDPOINT = "";
    
    @Override
    protected RequestSpecification getRequestSpec() {
        return RequestSpecs.getBooksRequestSpec();
    }
    
    @Step("Get all books")
    public Response getAllBooks() {
        return get(BOOKS_ENDPOINT);
    }
    
    @Step("Get book by ID: {bookId}")
    public Response getBookById(int bookId) {
        return getById(BOOKS_ENDPOINT, bookId);
    }
    
    @Step("Create new book")
    public Response createBook(Book book) {
        return post(BOOKS_ENDPOINT, book);
    }
    
    @Step("Update book with ID: {bookId}")
    public Response updateBook(int bookId, Book book) {
        return put(BOOKS_ENDPOINT, bookId, book);
    }
    
    @Step("Delete book with ID: {bookId}")
    public Response deleteBook(int bookId) {
        return delete(BOOKS_ENDPOINT, bookId);
    }
}
