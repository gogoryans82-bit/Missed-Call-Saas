package com.leadback.web;

import com.leadback.repo.BusinessRepository;
import com.leadback.repo.LeadRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final BusinessRepository businesses;
    private final LeadRepository leads;

    public AdminController(BusinessRepository businesses, LeadRepository leads) {
        this.businesses = businesses;
        this.leads = leads;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("businesses", businesses.findAll());
        model.addAttribute("totalLeads", leads.count());
        return "admin";
    }
}
