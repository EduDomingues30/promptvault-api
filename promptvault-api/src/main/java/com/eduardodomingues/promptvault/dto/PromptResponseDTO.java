package com.eduardodomingues.promptvault.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record PromptResponseDTO(
        Long id,
        String name,
        String description,
        String category,
        Set<String> tags,
        String latestVersion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
