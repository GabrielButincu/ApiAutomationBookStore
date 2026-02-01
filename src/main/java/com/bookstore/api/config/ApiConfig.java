package com.bookstore.api.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({
    "system:properties",
    "system:env",
    "classpath:config.properties"
})
public interface ApiConfig extends Config {
    
    @Key("base.uri")
    @DefaultValue("https://fakerestapi.azurewebsites.net")
    String baseUri();
    
    @Key("api.version")
    @DefaultValue("/api/v1")
    String apiVersion();
    
    @Key("api.timeout")
    @DefaultValue("30000")
    int timeout();
    
    @Key("books.endpoint")
    @DefaultValue("/Books")
    String booksEndpoint();
    
    @Key("authors.endpoint")
    @DefaultValue("/Authors")
    String authorsEndpoint();
    
    @Key("retry.count")
    @DefaultValue("2")
    int retryCount();
    
    @Key("parallel.execution")
    @DefaultValue("true")
    boolean isParallelExecution();
    
    @Key("thread.count")
    @DefaultValue("5")
    int threadCount();
    
    @Key("log.level")
    @DefaultValue("INFO")
    String logLevel();
    
    @Key("log.requests")
    @DefaultValue("true")
    boolean logRequests();
    
    @Key("log.responses")
    @DefaultValue("true")
    boolean logResponses();
    
    @Key("test.environment")
    @DefaultValue("QA")
    String environment();
    
    default String getFullUrl(String endpoint) {
        return baseUri() + apiVersion() + endpoint;
    }
}
