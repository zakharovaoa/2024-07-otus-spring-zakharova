package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
@ShellComponent(value = "Testing application")
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private TestResult testResult;

    private Student student;

    @ShellMethod(value = "Run determine current student", key = {"r", "run"})
    @Override
    public void run() {
        student = studentService.determineCurrentStudent();
    }

    @ShellMethod(value = "Test execute", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestingAvailable")
    public void testing() {
        testResult = testService.executeTestFor(this.student);
    }

    @ShellMethod(value = "Show result", key = {"s", "show"})
    @ShellMethodAvailability(value = "isResultAvailable")
    public void result() {
        resultService.showResult(this.testResult);
    }

    private Availability isTestingAvailable() {
        return (!(student == null) && !student.getFullName().isEmpty())
                ? Availability.available()
                : Availability.unavailable("Сначала введите ФИО");
    }

    private Availability isResultAvailable() {
        return !(testResult == null)
                ? Availability.available()
                : Availability.unavailable("Тест еще не проведён");
    }
}
