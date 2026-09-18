package com.leadback.web;

import com.leadback.domain.Business;
import com.leadback.repo.BusinessRepository;
import com.leadback.security.CurrentUserService;
import com.leadback.service.ProvisioningService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/setup")
public class SetupWizardController {

    private final CurrentUserService current;
    private final BusinessRepository businesses;
    private final ProvisioningService provisioning;

    public SetupWizardController(CurrentUserService current,
                                 BusinessRepository businesses,
                                 ProvisioningService provisioning) {
        this.current = current;
        this.businesses = businesses;
        this.provisioning = provisioning;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("business", current.currentBusiness());
        return "setup";
    }

    @PostMapping("/auto")
    public String autoProvision(@RequestParam String areaCode, RedirectAttributes ra) {
        Business b = current.currentBusiness();
        try {
            provisioning.provision(b, areaCode);
            b.setOnboardingComplete(true);
            businesses.save(b);
            ra.addFlashAttribute("msg", "Number provisioned: " + b.getTwilioNumber());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Auto-provision failed: " + e.getMessage()
                    + " — use manual entry below.");
        }
        return "redirect:/setup";
    }

    @PostMapping("/manual")
    public String manual(@RequestParam String twilioNumber, RedirectAttributes ra) {
        Business b = current.currentBusiness();
        if (businesses.existsByTwilioNumber(twilioNumber)
                && !twilioNumber.equals(b.getTwilioNumber())) {
            ra.addFlashAttribute("error", "That number is already in use.");
            return "redirect:/setup";
        }
        b.setTwilioNumber(twilioNumber);
        b.setOnboardingComplete(true);
        businesses.save(b);
        ra.addFlashAttribute("msg", "Saved. Point your Twilio voice + SMS webhooks to this app.");
        return "redirect:/setup";
    }
}
