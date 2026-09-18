package com.leadback.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "owner_phone", nullable = false)
    private String ownerPhone;

    @Column(name = "twilio_number", unique = true)
    private String twilioNumber;

    @Column(name = "twilio_subaccount_sid")
    private String twilioSubaccountSid;

    @Column(name = "twilio_subaccount_token")
    private String twilioSubaccountToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.TRIAL;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    @Column(name = "onboarding_complete", nullable = false)
    private boolean onboardingComplete = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lead> leads = new ArrayList<>();

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    public String getTwilioNumber() { return twilioNumber; }
    public void setTwilioNumber(String twilioNumber) { this.twilioNumber = twilioNumber; }

    public String getTwilioSubaccountSid() { return twilioSubaccountSid; }
    public void setTwilioSubaccountSid(String s) { this.twilioSubaccountSid = s; }

    public String getTwilioSubaccountToken() { return twilioSubaccountToken; }
    public void setTwilioSubaccountToken(String s) { this.twilioSubaccountToken = s; }

    public SubscriptionStatus getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(SubscriptionStatus s) { this.subscriptionStatus = s; }

    public String getStripeCustomerId() { return stripeCustomerId; }
    public void setStripeCustomerId(String s) { this.stripeCustomerId = s; }

    public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    public void setStripeSubscriptionId(String s) { this.stripeSubscriptionId = s; }

    public boolean isOnboardingComplete() { return onboardingComplete; }
    public void setOnboardingComplete(boolean b) { this.onboardingComplete = b; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<Lead> getLeads() { return leads; }
    public void setLeads(List<Lead> leads) { this.leads = leads; }
}
