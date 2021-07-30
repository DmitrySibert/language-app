package com.dsib.language.core.word.infrastructure.dao.spring.converter;

import com.dsib.language.core.word.infrastructure.dao.WordData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class JsonbToWordDataConverter implements Converter<PGobject, WordData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonbToWordDataConverter.class);

    private final ObjectMapper objectMapper;

    public JsonbToWordDataConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public WordData convert(PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), WordData.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("DAO error. PGobject to WordData conversation: ", e);
            throw new IllegalArgumentException("DAO error. PGobject to WordData conversation: ", e);
        }
    }
}
