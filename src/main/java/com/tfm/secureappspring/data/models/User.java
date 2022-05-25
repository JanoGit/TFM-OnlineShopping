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
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tfm_users")
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
    private String passwordHash;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    private String firstName;
    private String lastName;
    @Column(name = "address", length = 45)
    private String address;
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "tfmUsers")
    @ToString.Exclude
    private Set<Order> orders = new LinkedHashSet<>();

}