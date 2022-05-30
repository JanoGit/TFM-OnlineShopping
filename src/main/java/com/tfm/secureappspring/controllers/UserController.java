package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.daos.UserRepository;
import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/Users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/Index")
    public String showUsersList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "Users/Index";
    }

    @GetMapping(value = "/Login")
    public String showLoginPage(Model model) {
        if (model.containsAttribute("registeredUser")) {
            User registeredUser = (User) model.getAttribute("registeredUser");
            model.addAttribute("formUser", registeredUser);
            return "Users/Login";
        }
        model.addAttribute("formUser", new User());

        return "Users/Login";
    }

    @RequestMapping(value = "/Logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String showLogoutPage() {
        return "Users/Logout";
    }


    @GetMapping(value = "/Register")
    public String showRegisterPage() {

        return "Users/Register";
    }

    // POST: /Users/Register
    @RequestMapping(value="/Register", method=RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@RequestBody MultiValueMap<String, String> formData, Errors errors, RedirectAttributes redirectAttributes) {

        if (formData.get("register_user_mail").get(0).isBlank() ||
                formData.get("register_user_name").get(0).isBlank() ||
                formData.get("register_user_password").get(0).isBlank() ||
                formData.get("repeated_register_user_password").get(0).isBlank()) {
            redirectAttributes.addFlashAttribute("requiredFields", "All fields must be fulfilled.");

            return "redirect:/Users/Register";
        }

        if (!formData.get("register_user_password").get(0)
                .equals(formData.get("repeated_register_user_password").get(0))) {
            redirectAttributes.addFlashAttribute("passwordError", "Passwords don't match.");

            return "redirect:/Users/Register";
        }

        if (userRepository.findByMail(formData.get("register_user_mail").get(0)).isPresent()) {
            redirectAttributes.addFlashAttribute("userExists", "User already exists.");

            return "redirect:/Users/Register";
        }

        User user = User.builder()
                .mail(formData.get("register_user_mail").get(0))
                .password(formData.get("register_user_password").get(0))
                .role(Role.CUSTOMER)
                .userName(formData.get("register_user_name").get(0))
                .enabled(true)
                .registrationDate(LocalDateTime.now())
                .build();
        this.userRepository.save(user);

        redirectAttributes.addFlashAttribute("registeredUser", user);

        return "redirect:/Users/Login";
    }
}
