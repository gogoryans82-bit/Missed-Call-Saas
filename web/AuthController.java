package com.leadback.web;

import com.leadback.service.BusinessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final BusinessService businessService;

    public AuthController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String businessName,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String ownerPhone,
                         RedirectAttributes ra) {
        try {
            businessService.signup(businessName, email, password, ownerPhone);
            ra.addFlashAttribute("msg", "Account created. Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }
}
