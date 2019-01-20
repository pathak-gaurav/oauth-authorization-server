package com.gauravpathak;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private AccountRepository repository;
    private PasswordEncoder encoder;

    public CustomUserDetailService(AccountRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .map(x -> new User(x.getUsername(),
                        encoder.encode(x.getPassword()),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.createAuthorityList("read", "write")))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}
