package com.arsip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
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

import com.arsip.service.impl.CustomUserDevice;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    /* ==============================
     * 1️⃣ API / AUTH CONFIG (ORDER 1)
     * ============================== */
    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        private final CustomUserDevice userDevice;

        public ApiSecurityConfig(CustomUserDevice userDevice) {
            this.userDevice = userDevice;
        }

        /** 🔥 PENTING: LEWATI SECURITY UNTUK H2 */
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/h2-console/**");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDevice)
                .passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    }

    /* ==============================
     * 2️⃣ FORM LOGIN CONFIG (ORDER 2)
     * ============================== */
    @Configuration
    @Order(2)
    public static class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {

        /** 🔥 PENTING: LEWATI SECURITY UNTUK H2 */
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/h2-console/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/login", "/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .csrf().disable()
                .headers().frameOptions().disable();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public HttpFirewall httpFirewall() {
            return new DefaultHttpFirewall();
        }
    }
}