package com.leadback.service;

import com.leadback.config.TwilioProperties;
import com.leadback.domain.Business;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.Account;
import com.twilio.rest.api.v2010.account.AvailablePhoneNumberCountry;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningService {

    private static final Logger log = LoggerFactory.getLogger(ProvisioningService.class);

    private final TwilioProperties props;

    public ProvisioningService(TwilioProperties props) {
        this.props = props;
    }

    /**
     * Creates a Twilio subaccount for a business and buys an SMS+Voice number.
     * Returns a small result object. If anything fails, the caller should fall
     * back to manual number entry (SetupWizardController handles that).
     */
    public ProvisionResult provision(Business business, String areaCode) {
        String masterSid = props.getAccount().getSid();
        String masterToken = props.getAccount().getToken();
        String baseUrl = props.getWebhook().getBaseUrl();

        if (isBlank(masterSid) || isBlank(masterToken)) {
            throw new IllegalStateException("Twilio master credentials not configured");
        }

        Twilio.init(masterSid, masterToken);

        // 1) Create subaccount
        Account sub = Account.creator()
                .setFriendlyName("LeadBack - " + business.getName())
                .create();

        String subSid = sub.getSid();
        String subToken = sub.getAuthToken();

        // 2) Find an available local number
        ResourceSet<Local> available = Local.reader(
                        new PhoneNumber(props.getCountry().getCode()))
                .setAreaCode(areaCode)
                .setSmsEnabled(true)
                .setVoiceEnabled(true)
                .limit(1)
                .read();

        String e164 = null;
        for (Local l : available) {
            e164 = l.getPhoneNumber().toString();
            break;
        }

        if (e164 == null) {
            throw new IllegalStateException("No available numbers for " + areaCode);
        }

        // 3) Buy the number under the subaccount and wire webhooks
        Twilio.init(subSid, subToken);
        IncomingPhoneNumber number = IncomingPhoneNumber.creator(new PhoneNumber(e164))
                .setVoiceUrl(baseUrl + "/twilio/voice")
                .setVoiceMethod(org.springframework.http.HttpMethod.POST.name())
                .setSmsUrl(baseUrl + "/twilio/sms")
                .setSmsMethod(org.springframework.http.HttpMethod.POST.name())
                .create();

        business.setTwilioSubaccountSid(subSid);
        business.setTwilioSubaccountToken(subToken);
        business.setTwilioNumber(number.getPhoneNumber().toString());

        return new ProvisionResult(subSid, subToken, business.getTwilioNumber());
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    public record ProvisionResult(String subaccountSid, String subaccountToken, String phoneNumber) {}
}
