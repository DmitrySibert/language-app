package com.dsib.language.core.training.infrastructure.dao.spring.converter;

import com.dsib.language.core.training.domain.TrainingSet;
import com.dsib.language.core.word.infrastructure.dao.WordData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

//TODO: make a generic class Simple JSONB converter
@WritingConverter
@Component
public class TrainingSetToJsonbConverter implements Converter<TrainingSet, PGobject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingSetToJsonbConverter.class);

    private final ObjectMapper objectMapper;

    public TrainingSetToJsonbConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(TrainingSet source) {
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
