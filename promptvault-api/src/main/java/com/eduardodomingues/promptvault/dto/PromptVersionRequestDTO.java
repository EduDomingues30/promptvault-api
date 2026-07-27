package com.eduardodomingues.promptvault.dto;

import jakarta.validation.constraints.NotBlank;

public record PromptVersionRequestDTO(
        @NotBlank String version,
        @NotBlank String content,
        String changelog,
        String author
) {}
