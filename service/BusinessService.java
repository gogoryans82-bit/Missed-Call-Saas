package com.leadback.service;

import com.leadback.domain.AppUser;
import com.leadback.domain.Business;
import com.leadback.repo.AppUserRepository;
import com.leadback.repo.BusinessRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessService {

    private final BusinessRepository businesses;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public BusinessService(BusinessRepository businesses,
                           AppUserRepository users,
                           PasswordEncoder encoder) {
        this.businesses = businesses;
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public Business signup(String businessName, String email, String password, String ownerPhone) {
        if (businesses.existsByEmail(email) || users.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        Business b = new Business();
        b.setName(businessName);
        b.setEmail(email);
        b.setOwnerPhone(ownerPhone);
        businesses.save(b);

        AppUser u = new AppUser();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setRole("USER");
        u.setBusiness(b);
        users.save(u);

        return b;
    }
}
