package com.bookstore.api.clients;

import com.bookstore.api.models.Author;
import com.bookstore.api.specs.RequestSpecs;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthorsApiClient extends BaseApiClient {
    
    private static final String AUTHORS_ENDPOINT = "";
    
    @Override
    protected RequestSpecification getRequestSpec() {
        return RequestSpecs.getAuthorsRequestSpec();
    }
    
    @Step("Get all authors")
    public Response getAllAuthors() {
        return get(AUTHORS_ENDPOINT);
    }
    
    @Step("Get author by ID: {authorId}")
    public Response getAuthorById(int authorId) {
        return getById(AUTHORS_ENDPOINT, authorId);
    }
    
    @Step("Create new author")
    public Response createAuthor(Author author) {
        return post(AUTHORS_ENDPOINT, author);
    }
    
    @Step("Update author with ID: {authorId}")
    public Response updateAuthor(int authorId, Author author) {
        return put(AUTHORS_ENDPOINT, authorId, author);
    }
    
    @Step("Delete author with ID: {authorId}")
    public Response deleteAuthor(int authorId) {
        return delete(AUTHORS_ENDPOINT, authorId);
    }
    
    @Step("Get authors by book ID: {bookId}")
    public Response getAuthorsByBookId(int bookId) {
        return get("/authors/books/" + bookId);
    }
}
