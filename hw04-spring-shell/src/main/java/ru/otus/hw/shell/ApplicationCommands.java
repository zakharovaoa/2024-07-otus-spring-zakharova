package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;


@ShellComponent(value = "Testing Application Commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private TestResult testResult;

    private Student student;

    @ShellMethod(value = "Run determine current student", key = {"r", "run"})
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
        return (nonNull(student) && isNotEmpty(student.getFullName()))
                ? Availability.available()
                : Availability.unavailable("Сначала введите ФИО");
    }

    private Availability isResultAvailable() {
        return nonNull(testResult)
                ? Availability.available()
                : Availability.unavailable("Тест еще не проведён");
    }
}
