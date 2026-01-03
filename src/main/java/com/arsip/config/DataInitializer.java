package com.arsip.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arsip.entity.MstUser;
import com.arsip.repository.UserRepository;

@Component
public class DataInitializer {
	@Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        // Cek apakah superadmin sudah ada
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            MstUser admin = new MstUser();
            admin.setUsername("superadmin");
            admin.setPassword("superadmin123");
            admin.setRole("ADMIN");

            userRepository.save(admin);
        }
    }
}
