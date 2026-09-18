package com.leadback.bootstrap;

import com.leadback.domain.AppUser;
import com.leadback.repo.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataSeeder(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.findByEmail(adminEmail).isEmpty()) {
            AppUser admin = new AppUser();
            admin.setEmail(adminEmail);
            admin.setPasswordHash(encoder.encode(adminPassword));
            admin.setRole("ADMIN");
            users.save(admin);
            System.out.println("Seeded admin: " + adminEmail);
        }
    }
}
