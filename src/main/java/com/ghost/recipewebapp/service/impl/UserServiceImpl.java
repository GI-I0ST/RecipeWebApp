package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.exception.UserAlreadyExistException;
import com.ghost.recipewebapp.exception.UserNotFoundException;
import com.ghost.recipewebapp.repository.UserRepository;
import com.ghost.recipewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User newUser) {
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
}
