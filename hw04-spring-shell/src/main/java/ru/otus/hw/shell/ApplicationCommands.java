package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.service.TestRunnerService;


@ShellComponent(value = "Testing Application Commands")
@RequiredArgsConstructor
public class ApplicationCommands {
    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run determine current student", key = {"r", "run"})
    public void run() {
        testRunnerService.run();
    }

    @ShellMethod(value = "Test execute", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestingAvailable")
    public void testing() {
        testRunnerService.testing();
    }

    @ShellMethod(value = "Show result", key = {"s", "show"})
    @ShellMethodAvailability(value = "isResultAvailable")
    public void result() {
        testRunnerService.result();
    }

    public Availability isTestingAvailable() {
        return testRunnerService.isTestingAvailable();
    }

    public Availability isResultAvailable() {
        return testRunnerService.isResultAvailable();
    }
}
