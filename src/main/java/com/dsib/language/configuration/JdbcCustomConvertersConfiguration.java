package com.dsib.language.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

/**
 * This class only registers custom converters for Spring DAO classes
 * It can be only the single instance of JdbcCustomConversions. So it can be only
 * the single class across all project's modules and it is extracted as an outstanding
 * class outside any concrete module. Each module expose its own Converter<?,?>'s
 *
 * ConverterRegistry, ConversionService were considered to register converters, but Spring DAO doesn't have these converters
 *
 * Can be worked around using separate DataSources for each module.
 */
//TODO: Find a way to register converters within each module independently
@Configuration
public class JdbcCustomConvertersConfiguration extends AbstractJdbcConfiguration {

  private List<Converter<?,?>> converters;

    public JdbcCustomConvertersConfiguration(List<Converter<?,?>> converters) {
      this.converters = converters;
    }

    @Override
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(converters);
    }
}
