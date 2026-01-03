package com.arsip.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arsip.entity.MstUser;
import com.arsip.repository.UserRepository;

@Service
public class CustomUserDevice implements UserDetailsService{
	
	@Autowired
    private UserRepository repo;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<MstUser> list = repo.findByUsername(username);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        MstUser user = list.get(0);

        // {noop} jika password plain text
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password("{noop}" + user.getPassword())
                .roles(user.getRole())
                .build();
    }

}
