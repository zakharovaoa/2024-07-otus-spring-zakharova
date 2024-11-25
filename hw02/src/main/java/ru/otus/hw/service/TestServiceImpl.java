package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        var testResult = new TestResult(student);
        try {
            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");
            var questions = questionDao.findAll();
            askQuestions(questions, testResult);
        } catch (QuestionReadException questionReadException) {
            ioService.printLine("Ошибка чтения файла с вопросами");
        }
        return testResult;
    }

    private void askQuestions(List<Question> questions, TestResult testResult) {
        for (var question : questions) {
            printQuestion(question);
            var answerOfStudent = ioService.readString();
            var isAnswerValid = checkAnswer(question, answerOfStudent);
            testResult.applyAnswer(question, isAnswerValid);
        }
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        for (var answer : question.answers()) {
            ioService.printFormattedLine(answer.text());
        }
        ioService.printLine("");
    }

    private boolean checkAnswer(Question question, String answerOfStudent) {
        var correctAnswer = question.answers().stream().filter(Answer::isCorrect).findAny();
        return correctAnswer.isPresent() && correctAnswer.get().text().equals(answerOfStudent);
    }
}
