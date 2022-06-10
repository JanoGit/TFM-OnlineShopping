package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotBlank
    @Email
    @Column(name = "mail", length = 80, unique = true)
    private String mail;
    @NotBlank
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotBlank
    @Column(name = "username", length = 45)
    private String userName;
    @Column(name = "last_name", length = 45)
    private String lastName;
    @Column(name = "address", length = 45)
    private String address;
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();

}