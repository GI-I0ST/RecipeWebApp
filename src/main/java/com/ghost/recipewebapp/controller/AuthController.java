package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.dto.NewUserDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.exception.UserAlreadyExistException;
import com.ghost.recipewebapp.modelMapper.UserMapper;
import com.ghost.recipewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private boolean checkUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        // if user authenticated
        if (checkUserAuth()) {
            return "redirect:/recipes";
        }

        model.addAttribute("recipeSearch", new RecipeSearch());
        return "auth/login";
    }

    @GetMapping("/logout")
    public String getLogoutPage(Model model) {
        model.addAttribute("recipeSearch", new RecipeSearch());
        return "auth/logout";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        // if user authenticated
        if (checkUserAuth()) {
            return "redirect:/recipes";
        }

        model.addAttribute("user", new NewUserDto());
        model.addAttribute("recipeSearch", new RecipeSearch());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") NewUserDto newUserDto, BindingResult result, Model model) {
        // if user authenticated
        if (checkUserAuth()) {
            return "redirect:/recipes";
        }

        if (result.hasErrors()) {
            model.addAttribute("errorForm", true);
            model.addAttribute("recipeSearch", new RecipeSearch());
            return "auth/register";
        }

        try {
            User user = userMapper.toEntity(newUserDto);
            userService.saveUser(user);
        } catch (UserAlreadyExistException ex) {
            result.rejectValue("email", null, ex.getMessage());
            model.addAttribute("errorForm", true);
            model.addAttribute("recipeSearch", new RecipeSearch());
            return "auth/register";
        }

        return "redirect:/login";
    }
}
