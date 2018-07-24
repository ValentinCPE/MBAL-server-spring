package com.worldgether.mbal.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 *The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically
 *to the Spring Security filter chain.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .authorizeRequests()/*.anyRequest().authenticated().and()
                .formLogin().loginPage("/api/user/login").permitAll().and()
                .logout().deleteCookies("remember-me").permitAll().and()
                .rememberMe().tokenValiditySeconds(120);
                */.antMatchers("/private/**","/api/**").authenticated()
                .antMatchers("/","/home","/register","/login","/public/**").permitAll();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
