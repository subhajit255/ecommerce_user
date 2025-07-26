package com.service.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "description")
    private String description;
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
