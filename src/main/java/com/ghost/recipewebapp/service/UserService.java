package com.ghost.recipewebapp.service;


import com.ghost.recipewebapp.dto.NewUserDto;
import com.ghost.recipewebapp.entity.User;

import java.util.Optional;

public interface UserService {
    void saveUser(NewUserDto newUserDto);

    Optional<User> findUserByEmail(String email);
}
