package dev.sportsbetmini.betservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "users")
public class UserEntity {
    @Id private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    public UserEntity() {}
    public UserEntity(UUID id, String email) { this.id = id; this.email = email; }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public boolean isEmailVerified() { return emailVerified; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
}