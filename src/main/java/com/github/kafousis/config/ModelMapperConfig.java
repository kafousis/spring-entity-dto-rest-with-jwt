package com.github.kafousis.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /**
     * The main role of ModelMapper is to map objects by determining how one
     * object model is mapped to another called a Data Transformation Object (DTO).
     */

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
