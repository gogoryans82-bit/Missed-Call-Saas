package com.leadback.service;

import com.leadback.config.TwilioProperties;
import com.leadback.domain.Business;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private static final Logger log = LoggerFactory.getLogger(TwilioService.class);

    private final TwilioProperties props;

    public TwilioService(TwilioProperties props) {
        this.props = props;
    }

    /** Send from the platform master account (used for owner alerts). */
    public void sendFromMaster(String to, String from, String body) {
        if (isBlank(props.getAccount().getSid()) || isBlank(props.getAccount().getToken())) {
            log.warn("Twilio master creds missing — skipping SMS to {}", to);
            return;
        }
        Twilio.init(props.getAccount().getSid(), props.getAccount().getToken());
        Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
    }

    /** Send using a business's own subaccount (used for replies to callers). */
    public void sendFromBusiness(Business b, String to, String body) {
        String sid = b.getTwilioSubaccountSid();
        String token = b.getTwilioSubaccountToken();
        String from = b.getTwilioNumber();

        if (isBlank(sid) || isBlank(token) || isBlank(from)) {
            log.warn("Business {} not fully provisioned — skipping SMS", b.getId());
            return;
        }
        Twilio.init(sid, token);
        Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
    }

    /** Owner alert — uses master account, from business number. */
    public void notifyOwner(Business b, String body) {
        sendFromMaster(b.getOwnerPhone(), b.getTwilioNumber(), body);
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}
