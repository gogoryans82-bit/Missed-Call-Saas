package com.leadback.service;

import com.leadback.config.StripeProperties;
import com.leadback.domain.Business;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final StripeProperties props;

    public StripeService(StripeProperties props) {
        this.props = props;
    }

    public String createCheckoutUrl(Business b) throws Exception {
        Stripe.apiKey = props.getSecretKey();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(props.getSuccessUrl())
                .setCancelUrl(props.getCancelUrl())
                .setCustomerEmail(b.getEmail())
                .putMetadata("businessId", String.valueOf(b.getId()))
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(props.getPriceId())
                        .setQuantity(1L)
                        .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
