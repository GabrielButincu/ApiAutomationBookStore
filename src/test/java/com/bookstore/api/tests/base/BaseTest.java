package com.bookstore.api.tests.base;

import com.bookstore.api.clients.AuthorsApiClient;
import com.bookstore.api.clients.BooksApiClient;
import com.bookstore.api.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest {
    
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected BooksApiClient booksApi;
    protected AuthorsApiClient authorsApi;
    
    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        log.info("Setting up test class: {}", this.getClass().getSimpleName());

        booksApi = new BooksApiClient();
        authorsApi = new AuthorsApiClient();

        log.info("Test environment: {}", ConfigurationManager.getConfig().environment());
        log.info("Base URL: {}", ConfigurationManager.getConfig().baseUri());
    }
    
    @BeforeMethod(alwaysRun = true)
    public void setupMethod(Method method) {
        log.info("Starting test: {}", method.getName());
    }
}
