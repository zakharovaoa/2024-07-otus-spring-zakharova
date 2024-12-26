package ru.otus.hw.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {
    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(namedParameterJdbcOperations.queryForObject("""
                    select b.id, b.title, a.id author_id, a.full_name author_full_name, g.id genre_id,
                     g.name genre_name, from books b join authors a on a.id = b.author_id join genres g
                      on g.id = b.genre_id where b.id = :id""", Map.of("id", id), new BookRowMapper()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
                select b.id, b.title, a.id author_id, a.full_name author_full_name, g.id genre_id, g.name genre_name,
                 from books b join authors a on a.id = b.author_id join genres g on g.id = b.genre_id""",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "delete from books where id = :id", Map.of("id", id)
        );
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update("""
                        insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)""",
                params, keyHolder, new String[]{"id"});
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        namedParameterJdbcOperations.update("""
                update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id""",
                Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId())
        );
        return findById(book.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(book.getId())));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            var genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(rs.getLong("id"), rs.getString("title"), author, genre);
        }
    }
}
