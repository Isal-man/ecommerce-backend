package com.learn.ecommerce.security.service;

import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = usersRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " tidak ditemukan"));

        return UsersDetailsImpl.build(users);
    }
}
