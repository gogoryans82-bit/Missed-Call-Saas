package com.leadback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {

    private Account account = new Account();
    private Webhook webhook = new Webhook();
    private Country country = new Country();

    public static class Account {
        private String sid;
        private String token;
        public String getSid() { return sid; }
        public void setSid(String sid) { this.sid = sid; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    public static class Webhook {
        private String baseUrl;
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    }

    public static class Country {
        private String code;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Webhook getWebhook() { return webhook; }
    public void setWebhook(Webhook webhook) { this.webhook = webhook; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
}
