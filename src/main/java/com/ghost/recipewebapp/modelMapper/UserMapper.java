package com.ghost.recipewebapp.modelMapper;

import com.ghost.recipewebapp.dto.NewUserDto;
import com.ghost.recipewebapp.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewUserDto.class, User.class).addMappings(
                new PropertyMap<NewUserDto, User>() {
                    @Override
                    protected void configure() {
                        using(ctx ->
                                ((NewUserDto) ctx.getSource()).getFirstName() + " " + ((NewUserDto) ctx.getSource()).getLastName()
                        ).map(source, destination.getName());
                    }
                });
    }

    public User toEntity(NewUserDto userDto) {
       return modelMapper.map(userDto, User.class);
    }
}
