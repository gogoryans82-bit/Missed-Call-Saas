package com.leadback.web;

import com.leadback.domain.Business;
import com.leadback.security.CurrentUserService;
import com.leadback.service.StripeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillingController {

    private final CurrentUserService current;
    private final StripeService stripe;

    public BillingController(CurrentUserService current, StripeService stripe) {
        this.current = current;
        this.stripe = stripe;
    }

    @GetMapping("/billing")
    public String billing(Model model) {
        model.addAttribute("business", current.currentBusiness());
        return "dashboard";
    }

    @GetMapping("/billing/checkout")
    public String checkout() throws Exception {
        Business b = current.currentBusiness();
        String url = stripe.createCheckoutUrl(b);
        return "redirect:" + url;
    }

    @GetMapping("/billing/success")
    public String success() {
        return "redirect:/dashboard?upgraded=1";
    }

    @GetMapping("/billing/cancel")
    public String cancel() {
        return "redirect:/dashboard?cancelled=1";
    }
}
