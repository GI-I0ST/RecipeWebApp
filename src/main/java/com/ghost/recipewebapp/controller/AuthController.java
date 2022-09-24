package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.dto.NewUserDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
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
        model.addAttribute("user", new NewUserDto());
        model.addAttribute("recipeSearch", new RecipeSearch());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") NewUserDto newUserDto, BindingResult result, Model model) {
        Optional<User> existingUser = userService.findUserByEmail(newUserDto.getEmail());

        if (existingUser.isPresent()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("errorForm", true);
            model.addAttribute("recipeSearch", new RecipeSearch());
            return "auth/register";
        }

        userService.saveUser(newUserDto);
        return "redirect:/login";
    }
}
