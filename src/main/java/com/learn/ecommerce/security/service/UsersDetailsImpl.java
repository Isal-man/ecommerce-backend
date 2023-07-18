package com.learn.ecommerce.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learn.ecommerce.entity.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UsersDetailsImpl implements UserDetails {

    private String username;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String roles;

    public UsersDetailsImpl(String username, String email, String name, String password, String roles) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public static UsersDetailsImpl build(Users users) {
        return new UsersDetailsImpl(users.getId(), users.getEmail(), users.getName(), users.getPassword(), users.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.hasText(roles)) {
            String[] splits = roles.replaceAll(" ", " ").split(",");
            for (String string : splits) {
                authorities.add(new SimpleGrantedAuthority(string));
            }
        }

        return authorities;
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
