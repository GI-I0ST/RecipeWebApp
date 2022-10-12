package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.VerificationToken;
import com.ghost.recipewebapp.exception.UserAlreadyExistException;
import com.ghost.recipewebapp.exception.UserNotFoundException;
import com.ghost.recipewebapp.repository.UserRepository;
import com.ghost.recipewebapp.repository.VerificationTokenRepository;
import com.ghost.recipewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public void registerUser(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with email " + newUser.getEmail() + " is already exists");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setAuthorities("ROLE_USER");
        userRepository.save(newUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User findUserByVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        tokenRepository.save(verificationToken);
    }
}
