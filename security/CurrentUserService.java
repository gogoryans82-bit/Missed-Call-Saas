package com.leadback.security;

import com.leadback.domain.AppUser;
import com.leadback.domain.Business;
import com.leadback.repo.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final AppUserRepository users;

    public CurrentUserService(AppUserRepository users) {
        this.users = users;
    }

    public AppUser current() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return users.findByEmail(email).orElseThrow();
    }

    public Business currentBusiness() {
        Business b = current().getBusiness();
        if (b == null) throw new IllegalStateException("User has no business");
        return b;
    }
}
