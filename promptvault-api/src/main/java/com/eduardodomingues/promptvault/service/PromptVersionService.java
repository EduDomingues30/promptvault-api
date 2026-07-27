package com.eduardodomingues.promptvault.service;

import com.eduardodomingues.promptvault.dto.PromptVersionRequestDTO;
import com.eduardodomingues.promptvault.dto.PromptVersionResponseDTO;
import com.eduardodomingues.promptvault.exception.ResourceNotFoundException;
import com.eduardodomingues.promptvault.model.Prompt;
import com.eduardodomingues.promptvault.model.PromptVersion;
import com.eduardodomingues.promptvault.repository.PromptVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class PromptVersionService {

    private static final Pattern SEMVER = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");

    private final PromptVersionRepository versionRepository;
    private final PromptService promptService;

    public PromptVersionService(PromptVersionRepository versionRepository, PromptService promptService) {
        this.versionRepository = versionRepository;
        this.promptService = promptService;
    }

    public PromptVersionResponseDTO create(Long promptId, PromptVersionRequestDTO dto) {
        if (!SEMVER.matcher(dto.version()).matches()) {
            throw new IllegalArgumentException("Versão inválida. Use versionamento semântico (ex: 1.0.0).");
        }
        versionRepository.findByPromptIdAndVersion(promptId, dto.version())
                .ifPresent(v -> { throw new IllegalArgumentException("Versão já existe para este prompt: " + dto.version()); });

        Prompt prompt = promptService.getEntity(promptId);

        PromptVersion version = PromptVersion.builder()
                .prompt(prompt)
                .version(dto.version())
                .content(dto.content())
                .changelog(dto.changelog())
                .author(dto.author())
                .build();

        return toResponse(versionRepository.save(version));
    }

    public List<PromptVersionResponseDTO> listByPrompt(Long promptId) {
        promptService.getEntity(promptId);
        return versionRepository.findByPromptId(promptId).stream()
                .map(this::toResponse)
                .toList();
    }

    public PromptVersionResponseDTO findByPromptAndVersion(Long promptId, String version) {
        PromptVersion v = versionRepository.findByPromptIdAndVersion(promptId, version)
                .orElseThrow(() -> new ResourceNotFoundException("Versão não encontrada: " + version));
        return toResponse(v);
    }

    public void delete(Long versionId) {
        PromptVersion v = versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Versão não encontrada: " + versionId));
        versionRepository.delete(v);
    }

    public PromptVersion getEntityByPromptAndVersion(Long promptId, String version) {
        return versionRepository.findByPromptIdAndVersion(promptId, version)
                .orElseThrow(() -> new ResourceNotFoundException("Versão não encontrada: " + version));
    }

    private PromptVersionResponseDTO toResponse(PromptVersion v) {
        return new PromptVersionResponseDTO(
                v.getId(),
                v.getPrompt().getId(),
                v.getVersion(),
                v.getContent(),
                v.getChangelog(),
                v.getAuthor(),
                v.getCreatedAt()
        );
    }
}
