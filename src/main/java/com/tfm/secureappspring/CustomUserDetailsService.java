package com.tfm.secureappspring;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService {

    UserDetails loadUserByUsernameAndCheckCaptcha(String username, String captcha) throws UsernameNotFoundException;
}
