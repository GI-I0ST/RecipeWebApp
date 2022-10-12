package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.VerificationToken;

public interface UserService {
    void registerUser(User newUser);

    User findUserByEmail(String email);

    void saveRegisteredUser(User user);

    User findUserByVerificationToken(String verificationToken);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
