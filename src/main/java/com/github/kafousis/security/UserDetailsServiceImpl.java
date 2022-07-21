package com.github.kafousis.security;

import com.github.kafousis.entities.User;
import com.github.kafousis.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userByUsername = userRepository.findByUsernameJoinFetch(username);

        if (userByUsername.isPresent()){
            return new UserDetailsImpl(userByUsername.get());
        }else{
            log.error("Username not found in the database");
            throw new UsernameNotFoundException("Username not found in the database");
        }
    }
}
