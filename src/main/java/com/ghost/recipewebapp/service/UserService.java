package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.User;

public interface UserService {
    void saveUser(User newUser);

    User findUserByEmail(String email);
}
