package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.dto.NewUserDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.VerificationToken;
import com.ghost.recipewebapp.event.OnRegistrationCompleteEvent;
import com.ghost.recipewebapp.exception.UserAlreadyExistException;
import com.ghost.recipewebapp.modelMapper.UserMapper;
import com.ghost.recipewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
    public String registerUser(@Valid @ModelAttribute("user") NewUserDto newUserDto, BindingResult result, Model model, HttpServletRequest request) {
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
            // register user
            User user = userMapper.toEntity(newUserDto);
            userService.registerUser(user);

            // call event
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getContextPath()));
        } catch (UserAlreadyExistException ex) {
            result.rejectValue("email", null, ex.getMessage());
            model.addAttribute("errorForm", true);
            model.addAttribute("recipeSearch", new RecipeSearch());
            return "auth/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token doesn't exists");
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has been expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "redirect:/login";
    }
}
