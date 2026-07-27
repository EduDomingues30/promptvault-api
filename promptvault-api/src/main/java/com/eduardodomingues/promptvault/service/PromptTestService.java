package com.eduardodomingues.promptvault.service;

import com.eduardodomingues.promptvault.dto.PromptTestRequestDTO;
import com.eduardodomingues.promptvault.dto.PromptTestResponseDTO;
import com.eduardodomingues.promptvault.exception.ResourceNotFoundException;
import com.eduardodomingues.promptvault.model.Prompt;
import com.eduardodomingues.promptvault.model.PromptVersion;
import com.eduardodomingues.promptvault.repository.PromptVersionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;

@Service
public class PromptTestService {

    private final PromptService promptService;
    private final PromptVersionRepository versionRepository;

    public PromptTestService(PromptService promptService, PromptVersionRepository versionRepository) {
        this.promptService = promptService;
        this.versionRepository = versionRepository;
    }

    public PromptTestResponseDTO testPrompt(Long promptId, PromptTestRequestDTO req) {
        long start = System.currentTimeMillis();

        Prompt prompt = promptService.getEntity(promptId);

        PromptVersion version;
        if (req.version() != null && !req.version().isBlank()) {
            version = versionRepository.findByPromptIdAndVersion(promptId, req.version())
                    .orElseThrow(() -> new ResourceNotFoundException("Versão não encontrada: " + req.version()));
        } else {
            version = prompt.getVersions().stream()
                    .max(Comparator.comparing(v -> PromptService.semverKey(v.getVersion())))
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhuma versão disponível para este prompt."));
        }

        String rendered = renderTemplate(version.getContent(), req.variables());
        String provider = req.provider() == null ? "azure-openai" : req.provider().toLowerCase();
        String model = req.model() == null ? "default-model" : req.model();

        String simulated = switch (provider) {
            case "ollama" -> "[Ollama Simulation - model=" + model + "] Resposta simulada para o prompt renderizado.";
            case "azure-openai" -> "[Azure OpenAI Simulation - model=" + model + "] Resposta simulada para o prompt renderizado.";
            default -> "[Unknown Provider Simulation] Resposta simulada.";
        };

        long latency = System.currentTimeMillis() - start;

        return new PromptTestResponseDTO(
                prompt.getName(),
                version.getVersion(),
                provider,
                rendered,
                simulated,
                latency
        );
    }

    private String renderTemplate(String template, Map<String, String> variables) {
        if (template == null) return "";
        if (variables == null || variables.isEmpty()) return template;
        String result = template;
        for (Map.Entry<String, String> e : variables.entrySet()) {
            result = result.replace("{{" + e.getKey() + "}}", e.getValue() == null ? "" : e.getValue());
        }
        return result;
    }
}
