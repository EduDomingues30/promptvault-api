package com.eduardodomingues.promptvault.dto;

import java.util.Map;

public record PromptTestRequestDTO(
        String version,
        String provider,
        String model,
        Map<String, String> variables
) {}
