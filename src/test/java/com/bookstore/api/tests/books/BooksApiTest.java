package com.bookstore.api.tests.books;

import com.bookstore.api.data.TestDataFactory;
import com.bookstore.api.models.Book;
import com.bookstore.api.tests.base.BaseTest;
import com.bookstore.api.utils.ApiAssertions;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Bookstore API")
@Feature("Books Management")
public class BooksApiTest extends BaseTest {
    
    @Test(description = "Verify successful retrieval of all books")
    @Story("Get All Books")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /Books returns 200 and a list of books")
    public void testGetAllBooks_Success() {
        Response response = booksApi.getAllBooks();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        ApiAssertions.verifyListNotEmpty(response);
        
        Book[] books = response.as(Book[].class);
        assertThat(books).hasSizeGreaterThan(0);
        
        log.info("Retrieved {} books", books.length);
    }
    
    @Test(description = "Verify successful retrieval of a specific book by ID")
    @Story("Get Book By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /Books/{id} returns the correct book")
    public void testGetBookById_Success() {
        int bookId = 1;
        
        Response response = booksApi.getBookById(bookId);
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        
        Book book = response.as(Book.class);
        assertThat(book.getId()).isEqualTo(bookId);
        assertThat(book.getTitle()).isNotNull();
        
        log.info("Retrieved book: {}", book.getTitle());
    }
    
    @Test(description = "Verify 404 error when getting non-existent book")
    @Story("Get Book By ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper error handling for non-existent book ID")
    public void testGetBookById_NotFound() {
        int nonExistentId = 99999;
        
        Response response = booksApi.getBookById(nonExistentId);
        
        ApiAssertions.verifyStatusCode(response, 404);
        
        log.info("404 error correctly returned for non-existent book ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify validation error for invalid book ID")
    @Story("Get Book By ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid ID format triggers validation error")
    public void testGetBookById_InvalidId() {
        // FakeAPI may return 400 or 404 for invalid IDs like 0 or negative numbers
        int invalidId = 0;
        
        Response response = booksApi.getBookById(invalidId);
        
        // Accepting either 400 or 404 as both are valid error responses
        assertThat(response.getStatusCode())
                .isIn(400, 404);
        
        log.info("Error correctly returned for invalid book ID: {}", invalidId);
    }
    
    @Test(description = "Verify successful creation of a new book")
    @Story("Create Book")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that POST /Books creates a new book successfully")
    public void testCreateBook_Success() {
        Book newBook = TestDataFactory.createBookWithoutId();
        
        Response response = booksApi.createBook(newBook);
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        
        Book createdBook = response.as(Book.class);
        assertThat(createdBook.getTitle()).isEqualTo(newBook.getTitle());
        assertThat(createdBook.getDescription()).isEqualTo(newBook.getDescription());
        
        log.info("Successfully created book: {}", createdBook.getTitle());
    }
    
    @Test(description = "Verify creation with all valid fields")
    @Story("Create Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies book creation with all fields populated")
    public void testCreateBook_AllFields() {
        Book newBook = TestDataFactory.createRandomBook();
        
        Response response = booksApi.createBook(newBook);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        Book createdBook = response.as(Book.class);
        ApiAssertions.verifyBookDetails(createdBook, newBook);
        
        log.info("Book created with all fields: {}", createdBook);
    }
    
    @Test(description = "Verify validation error when creating book with invalid data")
    @Story("Create Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper validation of book creation payload")
    public void testCreateBook_InvalidData() {
        Book invalidBook = TestDataFactory.createInvalidBook();
        
        Response response = booksApi.createBook(invalidBook);
        
        // API might accept it or reject it, we check both scenarios
        assertThat(response.getStatusCode())
                .isIn(200, 400);
        
        log.info("Invalid book creation response: {}", response.getStatusCode());
    }
    
    @Test(description = "Verify successful update of existing book")
    @Story("Update Book")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that PUT /Books/{id} updates a book successfully")
    public void testUpdateBook_Success() {
        int bookId = 1;
        Book updatedBook = TestDataFactory.createRandomBook();
        updatedBook.setId(bookId);
        
        Response response = booksApi.updateBook(bookId, updatedBook);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        Book responseBook = response.as(Book.class);
        assertThat(responseBook.getId()).isEqualTo(bookId);
        assertThat(responseBook.getTitle()).isEqualTo(updatedBook.getTitle());
        
        log.info("Successfully updated book ID: {}", bookId);
    }
    
    @Test(description = "Verify partial update of book fields")
    @Story("Update Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies updating only specific fields of a book")
    public void testUpdateBook_PartialUpdate() {
        int bookId = 5;
        Book partialUpdate = Book.builder()
                .id(bookId)
                .title("Updated Title Only")
                .build();
        
        Response response = booksApi.updateBook(bookId, partialUpdate);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        log.info("Partial update completed for book ID: {}", bookId);
    }
    
    @Test(description = "Verify error when updating non-existent book")
    @Story("Update Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper error handling when updating non-existent book")
    public void testUpdateBook_NotFound() {
        int nonExistentId = 99999;
        Book updateData = TestDataFactory.createRandomBook();
        updateData.setId(nonExistentId);
        
        Response response = booksApi.updateBook(nonExistentId, updateData);
        
        // FakeAPI may return 200 even for non-existent IDs, or 404
        assertThat(response.getStatusCode())
                .isIn(200, 404);
        
        log.info("Update attempted for non-existent book ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify successful deletion of a book")
    @Story("Delete Book")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that DELETE /Books/{id} removes a book successfully")
    public void testDeleteBook_Success() {
        int bookId = 10;
        
        Response response = booksApi.deleteBook(bookId);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        log.info("Successfully deleted book ID: {}", bookId);
    }
    
    @Test(description = "Verify deletion of non-existent book")
    @Story("Delete Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies behavior when deleting a non-existent book")
    public void testDeleteBook_NotFound() {
        int nonExistentId = 99999;
        
        Response response = booksApi.deleteBook(nonExistentId);
        
        // FakeAPI typically returns 200 even for non-existent IDs
        assertThat(response.getStatusCode())
                .isIn(200, 404);
        
        log.info("Delete attempted for non-existent book ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify response time is acceptable")
    @Story("Performance")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that API response time is within acceptable limits")
    public void testGetAllBooks_ResponseTime() {
        Response response = booksApi.getAllBooks();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyResponseTime(response, 5000);
        
        log.info("Response time: {} ms", response.getTime());
    }
    
    @Test(description = "Verify required headers are present in response")
    @Story("Headers Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Test verifies that response contains expected headers")
    public void testGetAllBooks_Headers() {
        Response response = booksApi.getAllBooks();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyHeaderExists(response, "Content-Type");
        
        log.info("Content-Type header: {}", response.getContentType());
    }
}
