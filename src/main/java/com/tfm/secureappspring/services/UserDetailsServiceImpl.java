package com.tfm.secureappspring.services;

import com.tfm.secureappspring.data.daos.UserRepository;
import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Qualifier("tfm.users")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String mail) {
        User user = userRepository.findByMail(mail)
                .orElseThrow( () -> new UsernameNotFoundException("mail not found: " + mail));
        return this.userBuilder(user.getMail(), user.getPassword(), new Role[]{Role.AUTHENTICATED});
    }

    private org.springframework.security.core.userdetails.User userBuilder(String mail, String password, Role[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.withPrefix()));
        }
        return new org.springframework.security.core.userdetails.User(mail, password, true,  true,
                true, true, authorities);
    }
}
