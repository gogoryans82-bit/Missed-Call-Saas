package com.leadback.security;

import com.leadback.domain.AppUser;
import com.leadback.repo.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository users;

    public UserDetailsServiceImpl(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser u = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + email));

        return User.builder()
                .username(u.getEmail())
                .password(u.getPasswordHash())
                .roles(u.getRole())
                .build();
    }
}
