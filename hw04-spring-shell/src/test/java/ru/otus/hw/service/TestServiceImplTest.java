package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TestServiceImplTest {
    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testServiceImpl;

    @DisplayName("должен корректно проверять ответ")
    @Test
    void shouldCorrectCheckingAnswer() {
        var answer1 = new Answer("Flying Dutchman", false);
        var answer2 = new Answer("Nautilus", true);
        var answer3 = new Answer("Aurora", false);
        var answers = new ArrayList<>(Arrays.asList(answer1, answer2, answer3));
        var question = new Question("What is the name of the submarine in the novels of Jules Verne?", answers);
        var questions = List.of(question);
        given(ioService.readString()).willReturn("Nautilus");
        given(questionDao.findAll()).willReturn(questions);
        var student = new Student("Воробьёв", "Олег");
        var testResult = testServiceImpl.executeTestFor(student);
        assertEquals(1, testResult.getRightAnswersCount());
    }
}
