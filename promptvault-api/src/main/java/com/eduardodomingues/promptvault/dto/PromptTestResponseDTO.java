package com.eduardodomingues.promptvault.dto;

public record PromptTestResponseDTO(
        String promptName,
        String version,
        String provider,
        String renderedPrompt,
        String simulatedResponse,
        long latencyMs
) {}
