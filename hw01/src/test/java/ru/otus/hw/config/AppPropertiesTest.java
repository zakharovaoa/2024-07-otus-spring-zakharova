package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AppPropertiesTest {


    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        AppProperties appPropertiesTest = new AppProperties("TestFileName");

        assertEquals("TestFileName", appPropertiesTest.getTestFileName());
    }
}
