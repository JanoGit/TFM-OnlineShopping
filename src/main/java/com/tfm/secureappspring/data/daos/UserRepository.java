package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    Collection<User> findByRoleIn(Collection<Role> roles);
}