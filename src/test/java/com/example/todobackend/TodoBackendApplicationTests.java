package com.example.todobackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite::memory:")
class TodoBackendApplicationTests {
    @Test
    void contextLoads() {}
}
