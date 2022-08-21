package com.dsib.language.core.word.infrastructure.dao.spring.converter;

import com.dsib.language.core.word.infrastructure.dao.WordData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.SQLException;

@WritingConverter
public class WordDataToJsonbConverter implements Converter<WordData, PGobject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordDataToJsonbConverter.class);

    private final ObjectMapper objectMapper;

    public WordDataToJsonbConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(WordData source) {
        PGobject pGobject = new PGobject();
        pGobject.setType("jsonb");
        try {
            pGobject.setValue(objectMapper.writeValueAsString(source));
        } catch (SQLException | JsonProcessingException e) {
            LOGGER.error("DAO error. WordData to PGobject conversation: ", e);
            throw new IllegalArgumentException("DAO error. WordData to PGobject conversation: ", e);
        }
        return pGobject;
    }
}
