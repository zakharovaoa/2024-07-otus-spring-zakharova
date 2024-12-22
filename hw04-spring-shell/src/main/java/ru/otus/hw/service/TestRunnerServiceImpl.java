package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private TestResult testResult;

    private Student student;

    @Override
    public void run() {
        student = studentService.determineCurrentStudent();
    }

    @Override
    public void testing() {
        testResult = testService.executeTestFor(this.student);
    }

    @Override
    public void result() {
        resultService.showResult(this.testResult);
    }

    @Override
    public Availability isTestingAvailable() {
        return (nonNull(student) && isNotEmpty(student.getFullName()))
                ? Availability.available()
                : Availability.unavailable("Сначала введите ФИО");
    }

    @Override
    public Availability isResultAvailable() {
        return nonNull(testResult)
                ? Availability.available()
                : Availability.unavailable("Тест еще не проведён");
    }
}
