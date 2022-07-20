package com.github.kafousis.mapper;

import com.github.kafousis.dtos.UserDto;
import com.github.kafousis.entities.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

//@Configuration
public class UserMapper {

    /**
     * - The ModelMapper contains a TypeMap for each ordered pair of types
     * - The TypeMap contains a PropertyMap that has a single mapping
     */

    /**
     * THIS CODE IS NOT IN USE.
     * IT SHOWS HOW TO DO A CUSTOM MAPPING WITH ModelMapper.
     */

    @Autowired
    public UserMapper(ModelMapper modelMapper){

        // create a converter (String -> List<String>)
        Converter<String, List<String>> converter = context -> {
            return Arrays.asList(context.getSource().split(";"));
        };

        // add mapping that uses the converter to map certain fields of the objects
        modelMapper.addMappings(new PropertyMap<User, UserDto>() {

            @Override
            protected void configure() {
                using(converter).map(source.getRole(), destination.getRole());
            }
        });
    }
}
