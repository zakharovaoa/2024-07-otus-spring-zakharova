package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@SpringBootTest(classes = CsvQuestionDao.class)
public class CsvQuestionDaoTest {
    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("должен содержать корректное количество вопросов")
    @Test
    void shouldHaveCorrectCountQuestions() {
        given(fileNameProvider.getTestFileName()).willReturn("questionsTest.csv");
        assertEquals(5, csvQuestionDao.findAll().size());
    }
}
