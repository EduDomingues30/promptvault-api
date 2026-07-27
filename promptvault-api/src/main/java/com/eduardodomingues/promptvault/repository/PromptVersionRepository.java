package com.eduardodomingues.promptvault.repository;

import com.eduardodomingues.promptvault.model.PromptVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromptVersionRepository extends JpaRepository<PromptVersion, Long> {
    List<PromptVersion> findByPromptId(Long promptId);
    Optional<PromptVersion> findByPromptIdAndVersion(Long promptId, String version);
}
