package com.leadback.service;

import com.leadback.domain.Business;
import com.leadback.domain.Lead;
import com.leadback.repo.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeadService {

    private final LeadRepository leads;
    private final TwilioService twilio;

    public LeadService(LeadRepository leads, TwilioService twilio) {
        this.leads = leads;
        this.twilio = twilio;
    }

    @Transactional
    public Lead createMissedCall(Business b, String callerPhone) {
        Lead l = new Lead();
        l.setBusiness(b);
        l.setPhone(callerPhone);
        l.setState(0);
        l.setStatus("MISSED");
        return leads.save(l);
    }

    @Transactional
    public void handleIncomingSms(Business b, String phone, String body) {
        Lead lead = leads.findTopByBusinessAndPhoneOrderByCreatedAtDesc(b, phone)
                .orElseGet(() -> {
                    Lead l = new Lead();
                    l.setBusiness(b);
                    l.setPhone(phone);
                    l.setState(0);
                    l.setStatus("MISSED");
                    return leads.save(l);
                });

        String msg = body == null ? "" : body.trim();
        int state = lead.getState();

        switch (state) {
            case 0 -> {
                String service = switch (msg) {
                    case "1" -> "Quote";
                    case "2" -> "Booking";
                    case "3" -> "Emergency";
                    default -> msg;
                };
                lead.setService(service);
                lead.setState(1);
                lead.setStatus("IN_PROGRESS");
                leads.save(lead);
                twilio.sendFromBusiness(b, phone, "Great. What's your name?");
            }
            case 1 -> {
                lead.setName(msg);
                lead.setState(2);
                leads.save(lead);
                twilio.sendFromBusiness(b, phone, "What service do you need?");
            }
            case 2 -> {
                lead.setService(msg);
                lead.setState(3);
                leads.save(lead);
                twilio.sendFromBusiness(b, phone, "What's the address?");
            }
            case 3 -> {
                lead.setAddress(msg);
                lead.setState(4);
                leads.save(lead);
                twilio.sendFromBusiness(b, phone, "How urgent? Reply NOW, TODAY, or THIS WEEK.");
            }
            case 4 -> {
                lead.setUrgency(msg);
                lead.setState(5);
                lead.setStatus("NEW");
                leads.save(lead);
                twilio.sendFromBusiness(b, phone, "Thanks. We'll contact you shortly.");
                notifyOwner(b, lead);
            }
            default -> twilio.sendFromBusiness(b, phone,
                    "We got it. Someone will contact you shortly.");
        }
    }

    private void notifyOwner(Business b, Lead l) {
        String msg = "New lead: "
                + safe(l.getName()) + " | "
                + safe(l.getService()) + " | "
                + safe(l.getAddress()) + " | "
                + safe(l.getUrgency()) + " | "
                + safe(l.getPhone());
        twilio.notifyOwner(b, msg);
    }

    private String safe(String s) { return s == null || s.isBlank() ? "-" : s; }
}
