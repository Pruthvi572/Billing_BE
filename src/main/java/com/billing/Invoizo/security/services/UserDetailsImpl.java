package com.billing.Invoizo.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;


    private final String username;

    private final String email;

    @JsonIgnore
    private final String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

//    public static UserDetailsImpl build(EmployeeEntity user) {
//
//        return new UserDetailsImpl(
//                user.getEmployeeId(),
//                user.getEmail(),
//                user.getPassword());
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

}
