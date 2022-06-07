package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Collection<User> findByRoleIn(Collection<Role> roles);

    Optional<User> findByMail(String mail);

    @Query(value = "SELECT * FROM users WHERE mail = ?1", nativeQuery = true)
    User findUserByMail(String mail);
}