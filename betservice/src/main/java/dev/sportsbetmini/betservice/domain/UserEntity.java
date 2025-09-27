package dev.sportsbetmini.betservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "users")
public class UserEntity {
    @Id private UUID id;
    @Column(nullable = false, unique = true) private String email;
    @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt = OffsetDateTime.now();

    public UserEntity() {}
    public UserEntity(UUID id, String email) { this.id = id; this.email = email; }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
