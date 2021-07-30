package com.dsib.language.core.word;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class WordUpsertRepositoryImpl implements WordUpsertRepository {

    private JdbcTemplate jdbcTemplate;

    public WordUpsertRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] upsert(List<WordEntity> words) {
        return jdbcTemplate.batchUpdate("" +
                    "INSERT INTO word_entity(origin, translate, word_info, tags) VALUES (?, ?, ?, ?)\n" +
                    "ON CONFLICT(origin) DO UPDATE\n" +
                    "  SET translate = excluded.translate, \n" +
                    "      word_info = excluded.word_info, \n" +
                    "      tags = excluded.tags;",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, words.get(i).getOrigin());
                    ps.setString(2, words.get(i).getTranslate());
                    ps.setString(3, words.get(i).getWordInfo());
                    ps.setString(4, words.get(i).getTags());
                }
                @Override
                public int getBatchSize() {
                    return words.size();
                }
            });
    }
}
