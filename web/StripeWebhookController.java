package com.leadback.web;

import com.leadback.config.StripeProperties;
import com.leadback.domain.Business;
import com.leadback.domain.SubscriptionStatus;
import com.leadback.repo.BusinessRepository;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private final StripeProperties props;
    private final BusinessRepository businesses;

    public StripeWebhookController(StripeProperties props, BusinessRepository businesses) {
        this.props = props;
        this.businesses = businesses;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload,
                                          @RequestHeader("Stripe-Signature") String sig) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sig, props.getWebhookSecret());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            event.getDataObjectDeserializer().getObject().ifPresent(obj -> {
                Session session = (Session) obj;
                String bizId = session.getMetadata().get("businessId");
                if (bizId != null) {
                    businesses.findById(Long.valueOf(bizId)).ifPresent(b -> {
                        b.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                        b.setStripeCustomerId(session.getCustomer());
                        b.setStripeSubscriptionId(session.getSubscription());
                        businesses.save(b);
                    });
                }
            });
        }

        if ("customer.subscription.deleted".equals(event.getType())) {
            event.getDataObjectDeserializer().getObject().ifPresent(obj -> {
                com.stripe.model.Subscription sub = (com.stripe.model.Subscription) obj;
                businesses.findAll().stream()
                        .filter(b -> sub.getId().equals(b.getStripeSubscriptionId()))
                        .forEach(b -> {
                            b.setSubscriptionStatus(SubscriptionStatus.CANCELED);
                            businesses.save(b);
                        });
            });
        }

        return ResponseEntity.ok("ok");
    }
}
