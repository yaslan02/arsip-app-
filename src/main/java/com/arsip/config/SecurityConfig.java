package com.arsip.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import com.arsip.entity.MstUser;
import com.arsip.repository.UserRepository;
import com.arsip.service.impl.CustomUserDevice;

@Configuration
@EnableScheduling
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
//    	@Value("${security.user.name}")
//    	private String securityUserName;
//    	
//    	@Value("${security.user.password}")
//    	private String securityUserPassword;
    	
    	@Autowired
    	private UserRepository repo;
    	
    	@Autowired
    	private CustomUserDevice userDevice;
        
    	@Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDevice)
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        }
    }

    @Configuration
    @Order(2)
    public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter {

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {
    	    http
    	        .authorizeRequests()
    	            .antMatchers("/login", "/h2-console/**", "/css/**", "/js/**").permitAll()
    	            .anyRequest().authenticated()
    	            .and()
    	        .formLogin()
    	            .loginPage("/login")           // halaman login custom
    	            .defaultSuccessUrl("/dashboard", true)
    	            .permitAll()
    	            .and()
    	        .logout()
    	            .permitAll()
    	            .and()
    	        .csrf().disable()                 // optional untuk H2 console
    	        .headers().frameOptions().disable(); // optional untuk H2 console
    	}
    	
    	@Bean
        public HttpFirewall defaultHttpFirewall() {
        	return new DefaultHttpFirewall();
        }

    	
    	@Bean("authenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
               return super.authenticationManagerBean();
        }
    	
    	@Bean
    	public PasswordEncoder encoder() {
    		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	}

    }
}