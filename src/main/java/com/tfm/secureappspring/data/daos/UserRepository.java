package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Collection<User> findByRoleIn(Collection<Role> roles);

    Optional<User> findByMail(String mail);
}