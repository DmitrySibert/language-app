package com.dsib.language.core.progress.infrastructure.dao.spring;

import com.dsib.language.core.progress.infrastructure.dao.WordProgressEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WordProgressRepositoryExtensionImpl implements WordProgressRepositoryExtension {

  private final JdbcTemplate jdbcTemplate;

  public WordProgressRepositoryExtensionImpl(
    JdbcTemplate jdbcTemplate
  ) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int[] insertNotExisting(List<WordProgressEntity> words) {
    return jdbcTemplate.batchUpdate("" +
        "INSERT INTO repeat_word(owner_id, origin) VALUES (?, ?)\n" +
        "ON CONFLICT(origin) DO NOTHING;",
      new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setString(1, words.get(i).getOwnerId());
          ps.setString(2, words.get(i).getOrigin());
        }

        @Override
        public int getBatchSize() {
          return words.size();
        }
      });
  }
}
