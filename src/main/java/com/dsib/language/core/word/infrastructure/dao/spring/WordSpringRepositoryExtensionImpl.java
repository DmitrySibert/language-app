package com.dsib.language.core.word.infrastructure.dao.spring;

import com.dsib.language.core.word.infrastructure.dao.WordEntity;
import com.dsib.language.core.word.infrastructure.dao.spring.converter.JsonbToWordDataConverter;
import com.dsib.language.core.word.infrastructure.dao.spring.converter.WordDataToJsonbConverter;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class WordSpringRepositoryExtensionImpl implements WordSpringRepositoryExtension {

    private final JdbcTemplate jdbcTemplate;
    private final JsonbToWordDataConverter jsonbToWordDataConverter;
    private final WordDataToJsonbConverter wordDataToJsonbConverter;

    public WordSpringRepositoryExtensionImpl(
            JdbcTemplate jdbcTemplate,
            JsonbToWordDataConverter jsonbToWordDataConverter,
            WordDataToJsonbConverter wordDataToJsonbConverter
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonbToWordDataConverter = jsonbToWordDataConverter;
        this.wordDataToJsonbConverter = wordDataToJsonbConverter;
    }

    public int[] upsert(List<WordEntity> words) {
        return jdbcTemplate.batchUpdate("" +
                    "INSERT INTO word_entity(origin, data) VALUES (?, ?)\n" +
                    "ON CONFLICT(origin) DO UPDATE\n" +
                    "  SET data = word_entity.data;",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, words.get(i).getOrigin());
                    ps.setObject(2, wordDataToJsonbConverter.convert(words.get(i).getData()));
                }
                @Override
                public int getBatchSize() {
                    return words.size();
                }
            });
    }

    public List<WordEntity> findByTags(List<String> tags) {
        return jdbcTemplate.query(
            "SELECT origin, data FROM word_entity WHERE data->'tags' ??| ?::TEXT[]",
            preparedStatement -> {
                StringBuilder tagsSqlArray = new StringBuilder();
                tagsSqlArray.append("{");
                tags.forEach(tag -> tagsSqlArray.append("\"").append(tag).append("\"").append(","));
                tagsSqlArray.deleteCharAt(tagsSqlArray.lastIndexOf(","));
                tagsSqlArray.append("}");
                preparedStatement.setString(1, tagsSqlArray.toString());
            },
            result -> {
                List<WordEntity> entities = new LinkedList<>();
                while (result.next()) {
                    entities.add(new WordEntity(
                        result.getString("origin"),
                        jsonbToWordDataConverter.convert((PGobject) result.getObject("data")),
                        false
                    ));
                }
                return entities;
            }
        );
    }
}
