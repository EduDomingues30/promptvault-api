package com.eduardodomingues.promptvault.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prompt_versions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"prompt_id", "version"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(length = 1000)
    private String changelog;

    private String author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
