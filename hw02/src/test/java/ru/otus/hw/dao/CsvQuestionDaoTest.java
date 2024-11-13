package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class CsvQuestionDaoTest {
    private TestFileNameProvider fileNameProvider;

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @DisplayName("должен содержать корректное количество вопросов")
    @Test
    void shouldHaveCorrectCountQuestions() {
        given(fileNameProvider.getTestFileName()).willReturn("questionsTest.csv");
        assertEquals(5, csvQuestionDao.findAll().size());
    }
}
