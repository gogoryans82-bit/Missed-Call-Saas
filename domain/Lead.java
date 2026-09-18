package com.leadback.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "leads", indexes = {
    @Index(name = "idx_leads_business", columnList = "business_id"),
    @Index(name = "idx_leads_phone", columnList = "phone")
})
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false)
    private String phone;

    private String name;
    private String service;
    private String address;
    private String urgency;

    @Column(nullable = false)
    private String status = "MISSED"; // MISSED, IN_PROGRESS, NEW, CLOSED

    @Column(nullable = false)
    private int state = 0;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Business getBusiness() { return business; }
    public void setBusiness(Business business) { this.business = business; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getState() { return state; }
    public void setState(int state) { this.state = state; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
