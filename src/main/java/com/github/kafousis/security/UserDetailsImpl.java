package com.github.kafousis.security;

import com.github.kafousis.entities.Privilege;
import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // set authority from role (ROLE_name)
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRole().getName());
        authorities.add(roleAuthority);

        // extract list of privileges
        Role role = user.getRole();
        Set<Privilege> privileges = role.getPrivileges();

        privileges.forEach(auth -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(auth.getName());
            authorities.add(authority);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
