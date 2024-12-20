package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (inputStream == null) {
                throw new QuestionReadException("Error reading the file with questions");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            var resultList = new CsvToBeanBuilder<QuestionDto>(bufferedReader)
                    .withType(QuestionDto.class).withSeparator(';')
                    .withSkipLines(1).build().parse();
            List<Question> questionList = new ArrayList<>();
            for (QuestionDto item : resultList) {
                questionList.add(item.toDomainObject());
            }
            return questionList;
        } catch (IOException e) {
            throw new QuestionReadException("Error reading the file with questions", e);
        }
    }
}
