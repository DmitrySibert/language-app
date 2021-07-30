package com.dsib.language.core.training.infrastructure.dao.spring.converter;

import com.dsib.language.core.training.domain.TrainingSet;
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
public class JsonbToTrainingSetConverter implements Converter<PGobject, TrainingSet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonbToTrainingSetConverter.class);

    private final ObjectMapper objectMapper;

    public JsonbToTrainingSetConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public TrainingSet convert(PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), TrainingSet.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("DAO error. PGobject to WordData conversation: ", e);
            throw new IllegalArgumentException("DAO error. PGobject to WordData conversation: ", e);
        }
    }
}
