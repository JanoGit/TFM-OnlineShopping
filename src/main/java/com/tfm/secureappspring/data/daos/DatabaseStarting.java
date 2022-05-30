package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DatabaseStarting {

    private static final String MAIL = "admin@admin.com";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";
    private final UserRepository userRepository;

    @Autowired
    public DatabaseStarting(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.initialize();
    }

    void initialize() {
        LogManager.getLogger(this.getClass()).warn("------- Finding Admin -----------");
        if (this.userRepository.findByRoleIn(List.of(Role.ADMIN)).isEmpty()) {
            User user = User.builder().mail(MAIL).password(PASSWORD) // new BCryptPasswordEncoder().encode(PASSWORD)
                    .role(Role.ADMIN).userName(USERNAME).enabled(Boolean.TRUE).registrationDate(LocalDateTime.now()).build();
            this.userRepository.save(user);
            LogManager.getLogger(this.getClass()).warn("------- Created Admin -----------");
        }
    }
}
