package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@Configuration
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("#{T(Integer).parseInt('${test.rightAnswersCountToPass}')}")
    private int rightAnswersCountToPass;

    @Value("${test.fileName}")
    private String testFileName;
}
