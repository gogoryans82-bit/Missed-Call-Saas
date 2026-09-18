package com.leadback.web;

import com.leadback.domain.Business;
import com.leadback.repo.BusinessRepository;
import com.leadback.service.LeadService;
import com.leadback.service.TwilioService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/twilio")
public class VoiceController {

    private final BusinessRepository businesses;
    private final LeadService leadService;
    private final TwilioService twilio;

    public VoiceController(BusinessRepository businesses,
                           LeadService leadService,
                           TwilioService twilio) {
        this.businesses = businesses;
        this.leadService = leadService;
        this.twilio = twilio;
    }

    @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public String voice(@RequestParam("To") String to) {
        Business b = businesses.findByTwilioNumber(to).orElse(null);
        if (b == null) {
            return "<Response><Say>This number is not configured.</Say></Response>";
        }
        String actionUrl = "/twilio/voice/complete?biz=" + b.getId();
        return "<Response><Dial timeout=\"15\" action=\"" + actionUrl + "\" method=\"POST\">"
                + b.getOwnerPhone()
                + "</Dial></Response>";
    }

    @PostMapping(value = "/voice/complete", produces = MediaType.APPLICATION_XML_VALUE)
    public String complete(@RequestParam("From") String from,
                           @RequestParam("DialCallStatus") String status,
                           @RequestParam("biz") Long businessId) {

        if (List.of("no-answer", "busy", "failed", "canceled").contains(status)) {
            Business b = businesses.findById(businessId).orElse(null);
            if (b != null) {
                leadService.createMissedCall(b, from);
                twilio.sendFromBusiness(b, from,
                    "Sorry we missed you! What do you need? Reply 1 for quote, 2 to book, 3 for emergency.");
            }
        }
        return "<Response></Response>";
    }
}
