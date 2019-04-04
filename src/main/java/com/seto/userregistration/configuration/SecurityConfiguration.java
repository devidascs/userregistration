package com.seto.userregistration.configuration;

import com.seto.userregistration.model.User;
import com.seto.userregistration.repo.UserRepository;
import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Autowired
    private DataSource datasource;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/h2-console/**").authorizeRequests().anyRequest().permitAll();
        http.authorizeRequests().antMatchers("/**").access("hasRole('USER')");
        http.authorizeRequests().antMatchers("/actuator/**").access("hasRole('ADMIN')");


        http.authorizeRequests().anyRequest().authenticated();
        http.rememberMe().userDetailsService(userDetailsService);
        http.httpBasic();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN");
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            LOGGER.info("Preloading " + repository.save(new User("admin", "admin","ADMIN")));
            LOGGER.info("Preloading " + repository.save(new User("user", "password","USER")));
        };
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

}