package com.example.Notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class NotesApplicationTests {

    @Test
    void contextLoads() {
        // Test will pass if the Spring context loads successfully
    }
}
