package com.forex.forex_system.pattern.singleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class DatabaseConfig {
    private static DatabaseConfig instance;
    @Value("${spring.datasource.url}") private String dbUrl;
    @Value("${spring.datasource.username}") private String dbUsername;
    private DatabaseConfig() {}
    public static DatabaseConfig getInstance(DatabaseConfig bean) {
        if (instance == null) { instance = bean; }
        return instance;
    }
    public String getDbUrl() { return dbUrl; }
    public String getDbUsername() { return dbUsername; }
}

