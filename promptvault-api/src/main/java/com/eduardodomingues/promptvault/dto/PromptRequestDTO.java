package com.eduardodomingues.promptvault.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PromptRequestDTO(
        @NotBlank String name,
        String description,
        String category,
        Set<String> tags
) {}
