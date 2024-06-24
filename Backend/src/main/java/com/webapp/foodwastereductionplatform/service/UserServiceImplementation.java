package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.repositories.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import  com.webapp.foodwastereductionplatform.model.User;
import java.util.*;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with this email: " + username);
        }
        System.out.println("Loaded user: " + user.getEmail() + ", Role: " + user.getUserType());

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities

        );
    }
}

