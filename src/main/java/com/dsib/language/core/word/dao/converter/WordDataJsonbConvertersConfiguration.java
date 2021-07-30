package com.dsib.language.core.word.dao.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
public class WordDataJsonbConvertersConfiguration extends AbstractJdbcConfiguration {

    private final JsonbToWordDataConverter jsonbToWordDataConverter;
    private final WordDataToJsonbConverter wordDataToJsonbConverter;


    public WordDataJsonbConvertersConfiguration(ObjectMapper objectMapper) {
        jsonbToWordDataConverter = new JsonbToWordDataConverter(objectMapper);
        wordDataToJsonbConverter = new WordDataToJsonbConverter(objectMapper);
    }

    @Bean
    public JsonbToWordDataConverter jsonbToWordDataConverter() {
        return jsonbToWordDataConverter;
    }

    @Bean
    public WordDataToJsonbConverter wordDataToJsonbConverter() {
        return wordDataToJsonbConverter;
    }

    @Override
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(
                List.of(jsonbToWordDataConverter, wordDataToJsonbConverter)
        );
    }
}
