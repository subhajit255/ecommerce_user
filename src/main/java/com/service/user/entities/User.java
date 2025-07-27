package com.service.user.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;
    @Column(name = "phone_code")
    private String phoneCode;
    @Column(name = "phone")
    private String phone;
    @Column(name = "phone_verified_at")
    private LocalDateTime phoneVerifiedAt;
    @Column(name = "password")
    private String password;
    @Column(name = "image")
    private String image;
    @Column(name = "verification_code")
    private Short verificationCode;
    @Column(name = "status")
    private boolean status = true;
    @Column(name = "is_account_expired")
    private boolean accountExpired = false;
    @Column(name = "is_account_locked")
    private boolean accountLocked = false;
    @Column(name = "is_credential_expired")
    private boolean credentialExpired = false;
    @Column(name = "is_available")
    private boolean available = true;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "reg_src")
    private String regSrc;
    // relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserAddress> userAddresses = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    private Set<Role> roles = new HashSet<>();
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
