package com.eduardodomingues.promptvault.dto;

import java.time.LocalDateTime;

public record PromptVersionResponseDTO(
        Long id,
        Long promptId,
        String version,
        String content,
        String changelog,
        String author,
        LocalDateTime createdAt
) {}
