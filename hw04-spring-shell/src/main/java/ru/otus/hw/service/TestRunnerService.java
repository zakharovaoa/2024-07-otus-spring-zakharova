package ru.otus.hw.service;

import org.springframework.shell.Availability;

public interface TestRunnerService {
    void run();

    void testing();

    void result();

    Availability isTestingAvailable();

    Availability isResultAvailable();
}
