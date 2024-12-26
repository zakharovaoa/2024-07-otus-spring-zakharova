package ru.otus.hw.commands;


import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DbConsoleCommand {

    @ShellMethod(value = "Console for DB", key = "c")
    public void dbConsole() throws Exception {
        Console.main();
    }
}
