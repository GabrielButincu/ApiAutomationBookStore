package com.bookstore.api.tests.authors;

import com.bookstore.api.data.TestDataFactory;
import com.bookstore.api.models.Author;
import com.bookstore.api.tests.base.BaseTest;
import com.bookstore.api.utils.ApiAssertions;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Bookstore API")
@Feature("Authors Management")
public class AuthorsApiTest extends BaseTest {
    
    @Test(description = "Verify successful retrieval of all authors")
    @Story("Get All Authors")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /Authors returns 200 and a list of authors")
    public void testGetAllAuthors_Success() {
        Response response = authorsApi.getAllAuthors();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        ApiAssertions.verifyListNotEmpty(response);
        
        Author[] authors = response.as(Author[].class);
        assertThat(authors).hasSizeGreaterThan(0);
        
        log.info("Retrieved {} authors", authors.length);
    }
    
    @Test(description = "Verify successful retrieval of a specific author by ID")
    @Story("Get Author By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that GET /Authors/{id} returns the correct author")
    public void testGetAuthorById_Success() {
        int authorId = 1;
        
        Response response = authorsApi.getAuthorById(authorId);
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        
        Author author = response.as(Author.class);
        assertThat(author.getId()).isEqualTo(authorId);
        assertThat(author.getFirstName()).isNotNull();
        
        log.info("Retrieved author: {} {}", author.getFirstName(), author.getLastName());
    }
    
    @Test(description = "Verify 404 error when getting non-existent author")
    @Story("Get Author By ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper error handling for non-existent author ID")
    public void testGetAuthorById_NotFound() {
        int nonExistentId = 99999;
        
        Response response = authorsApi.getAuthorById(nonExistentId);
        
        ApiAssertions.verifyStatusCode(response, 404);
        
        log.info("404 error correctly returned for non-existent author ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify validation error for invalid author ID")
    @Story("Get Author By ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that invalid ID format triggers validation error")
    public void testGetAuthorById_InvalidId() {
        int invalidId = 0;
        
        Response response = authorsApi.getAuthorById(invalidId);
        
        assertThat(response.getStatusCode())
                .isIn(400, 404);
        
        log.info("Error correctly returned for invalid author ID: {}", invalidId);
    }
    
    @Test(description = "Verify successful creation of a new author")
    @Story("Create Author")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that POST /Authors creates a new author successfully")
    public void testCreateAuthor_Success() {
        Author newAuthor = TestDataFactory.createAuthorWithoutId();
        
        Response response = authorsApi.createAuthor(newAuthor);
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyJsonContentType(response);
        
        Author createdAuthor = response.as(Author.class);
        assertThat(createdAuthor.getFirstName()).isEqualTo(newAuthor.getFirstName());
        assertThat(createdAuthor.getLastName()).isEqualTo(newAuthor.getLastName());
        
        log.info("Successfully created author: {} {}", 
                createdAuthor.getFirstName(), createdAuthor.getLastName());
    }
    
    @Test(description = "Verify creation with all valid fields")
    @Story("Create Author")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies author creation with all fields populated")
    public void testCreateAuthor_AllFields() {
        Author newAuthor = TestDataFactory.createRandomAuthor();
        
        Response response = authorsApi.createAuthor(newAuthor);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        Author createdAuthor = response.as(Author.class);
        ApiAssertions.verifyAuthorDetails(createdAuthor, newAuthor);
        
        log.info("Author created with all fields: {}", createdAuthor);
    }
    
    @Test(description = "Verify validation error when creating author with invalid data")
    @Story("Create Author")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper validation of author creation payload")
    public void testCreateAuthor_InvalidData() {
        Author invalidAuthor = TestDataFactory.createInvalidAuthor();
        
        Response response = authorsApi.createAuthor(invalidAuthor);
        
        assertThat(response.getStatusCode())
                .isIn(200, 400);
        
        log.info("Invalid author creation response: {}", response.getStatusCode());
    }
    
    @Test(description = "Verify successful update of existing author")
    @Story("Update Author")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that PUT /Authors/{id} updates an author successfully")
    public void testUpdateAuthor_Success() {
        int authorId = 1;
        Author updatedAuthor = TestDataFactory.createRandomAuthor();
        updatedAuthor.setId(authorId);
        
        Response response = authorsApi.updateAuthor(authorId, updatedAuthor);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        Author responseAuthor = response.as(Author.class);
        assertThat(responseAuthor.getId()).isEqualTo(authorId);
        assertThat(responseAuthor.getFirstName()).isEqualTo(updatedAuthor.getFirstName());
        
        log.info("Successfully updated author ID: {}", authorId);
    }
    
    @Test(description = "Verify partial update of author fields")
    @Story("Update Author")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies updating only specific fields of an author")
    public void testUpdateAuthor_PartialUpdate() {
        int authorId = 5;
        Author partialUpdate = Author.builder()
                .id(authorId)
                .firstName("Updated First Name")
                .build();
        
        Response response = authorsApi.updateAuthor(authorId, partialUpdate);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        log.info("Partial update completed for author ID: {}", authorId);
    }
    
    @Test(description = "Verify error when updating non-existent author")
    @Story("Update Author")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies proper error handling when updating non-existent author")
    public void testUpdateAuthor_NotFound() {
        int nonExistentId = 99999;
        Author updateData = TestDataFactory.createRandomAuthor();
        updateData.setId(nonExistentId);
        
        Response response = authorsApi.updateAuthor(nonExistentId, updateData);
        
        assertThat(response.getStatusCode())
                .isIn(200, 404);
        
        log.info("Update attempted for non-existent author ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify successful deletion of an author")
    @Story("Delete Author")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies that DELETE /Authors/{id} removes an author successfully")
    public void testDeleteAuthor_Success() {
        int authorId = 10;
        
        Response response = authorsApi.deleteAuthor(authorId);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        log.info("Successfully deleted author ID: {}", authorId);
    }
    
    @Test(description = "Verify deletion of non-existent author")
    @Story("Delete Author")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies behavior when deleting a non-existent author")
    public void testDeleteAuthor_NotFound() {
        int nonExistentId = 99999;
        
        Response response = authorsApi.deleteAuthor(nonExistentId);
        
        assertThat(response.getStatusCode())
                .isIn(200, 404);
        
        log.info("Delete attempted for non-existent author ID: {}", nonExistentId);
    }
    
    @Test(description = "Verify response time is acceptable")
    @Story("Performance")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies that API response time is within acceptable limits")
    public void testGetAllAuthors_ResponseTime() {
        Response response = authorsApi.getAllAuthors();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyResponseTime(response, 5000);
        
        log.info("Response time: {} ms", response.getTime());
    }
    
    @Test(description = "Verify required headers are present in response")
    @Story("Headers Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Test verifies that response contains expected headers")
    public void testGetAllAuthors_Headers() {
        Response response = authorsApi.getAllAuthors();
        
        ApiAssertions.verifyStatusCode(response, 200);
        ApiAssertions.verifyHeaderExists(response, "Content-Type");
        
        log.info("Content-Type header: {}", response.getContentType());
    }
    
    @Test(description = "Verify retrieving authors by book ID")
    @Story("Get Authors By Book")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies retrieval of authors associated with a specific book")
    public void testGetAuthorsByBookId_Success() {
        int bookId = 1;
        
        Response response = authorsApi.getAuthorsByBookId(bookId);
        
        ApiAssertions.verifyStatusCode(response, 200);
        
        Author[] authors = response.as(Author[].class);
        assertThat(authors).isNotNull();
        
        log.info("Retrieved {} authors for book ID: {}", authors.length, bookId);
    }
}
