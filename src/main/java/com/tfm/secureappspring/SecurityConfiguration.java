package com.tfm.secureappspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(@Qualifier("tfm.users") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .csrf().disable().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        http
                /*.authorizeRequests()
                    .antMatchers("/", "/Home", "/Index")
                    .permitAll()
                    .and()*/
                .authorizeRequests()
                    .anyRequest()
                    .permitAll()
                    .and()
                .formLogin()
                    .loginPage("/Users/Login")
                    .loginProcessingUrl("/Users/Login")
                    .failureUrl("/Users/Login?error")
                    .successForwardUrl("/Users/Logout")
                    .usernameParameter("mail")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/Users/Logout")
                    .logoutSuccessUrl("/Users/Login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .and();
                /*.authorizeRequests()
                    .anyRequest()
                    .authenticated();*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}
