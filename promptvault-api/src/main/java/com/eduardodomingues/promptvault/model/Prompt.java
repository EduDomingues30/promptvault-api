package com.eduardodomingues.promptvault.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "prompts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptVersion> versions = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "prompt_tags",
        joinColumns = @JoinColumn(name = "prompt_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
