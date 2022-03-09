package tacos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.repositories.UserRepository;
import tacos.security.RegistrationForm;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "registration";
        }
        userRepo.save(form.toUser(encoder));
        return "redirect:/login";
    }

    @ModelAttribute(name = "registrationForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }
}
