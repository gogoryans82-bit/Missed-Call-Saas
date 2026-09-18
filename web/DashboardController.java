package com.leadback.web;

import com.leadback.domain.Business;
import com.leadback.repo.LeadRepository;
import com.leadback.security.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CurrentUserService current;
    private final LeadRepository leads;

    public DashboardController(CurrentUserService current, LeadRepository leads) {
        this.current = current;
        this.leads = leads;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Business b = current.currentBusiness();
        model.addAttribute("business", b);
        model.addAttribute("leadCount", leads.countByBusiness(b));
        return "dashboard";
    }

    @GetMapping("/leads")
    public String leads(Model model) {
        Business b = current.currentBusiness();
        model.addAttribute("business", b);
        model.addAttribute("leads", leads.findAllByBusinessOrderByCreatedAtDesc(b));
        return "leads";
    }
}
