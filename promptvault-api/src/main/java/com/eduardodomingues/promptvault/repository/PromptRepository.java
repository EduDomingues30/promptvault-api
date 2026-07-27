package com.eduardodomingues.promptvault.repository;

import com.eduardodomingues.promptvault.model.Prompt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    Page<Prompt> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Prompt> findByCategory(String category, Pageable pageable);
    Optional<Prompt> findByName(String name);
}
