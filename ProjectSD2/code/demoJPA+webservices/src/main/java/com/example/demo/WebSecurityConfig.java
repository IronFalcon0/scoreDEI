package com.example.demo;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/addUser").hasAnyAuthority("ADMIN")
            .antMatchers("/addTeam").hasAnyAuthority("ADMIN")
            .antMatchers("/addPlayer").hasAnyAuthority("ADMIN")
            .antMatchers("/addGame").hasAnyAuthority("ADMIN")
            .antMatchers("/getData").hasAnyAuthority("ADMIN")
            .antMatchers("/gameInfo/addEvent").hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/gameInfo/changeGameStatus").hasAnyAuthority("ADMIN", "USER")
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403")
            ;
    }
}
