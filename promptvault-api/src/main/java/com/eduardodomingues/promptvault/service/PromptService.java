package com.eduardodomingues.promptvault.service;

import com.eduardodomingues.promptvault.dto.PromptRequestDTO;
import com.eduardodomingues.promptvault.dto.PromptResponseDTO;
import com.eduardodomingues.promptvault.exception.ResourceNotFoundException;
import com.eduardodomingues.promptvault.model.Prompt;
import com.eduardodomingues.promptvault.model.Tag;
import com.eduardodomingues.promptvault.repository.PromptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromptService {

    private final PromptRepository promptRepository;
    private final TagService tagService;

    public PromptService(PromptRepository promptRepository, TagService tagService) {
        this.promptRepository = promptRepository;
        this.tagService = tagService;
    }

    public PromptResponseDTO create(PromptRequestDTO dto) {
        Prompt prompt = Prompt.builder()
                .name(dto.name())
                .description(dto.description())
                .category(dto.category())
                .build();

        if (dto.tags() != null) {
            Set<Tag> tags = dto.tags().stream()
                    .map(tagService::findOrCreate)
                    .collect(Collectors.toSet());
            prompt.setTags(tags);
        }

        return toResponse(promptRepository.save(prompt));
    }

    public Page<PromptResponseDTO> findAll(Pageable pageable) {
        return promptRepository.findAll(pageable).map(this::toResponse);
    }

    public PromptResponseDTO findById(Long id) {
        return toResponse(getEntity(id));
    }

    public PromptResponseDTO update(Long id, PromptRequestDTO dto) {
        Prompt prompt = getEntity(id);
        if (dto.name() != null) prompt.setName(dto.name());
        if (dto.description() != null) prompt.setDescription(dto.description());
        if (dto.category() != null) prompt.setCategory(dto.category());
        if (dto.tags() != null) {
            Set<Tag> tags = dto.tags().stream()
                    .map(tagService::findOrCreate)
                    .collect(Collectors.toSet());
            prompt.setTags(tags);
        }
        return toResponse(promptRepository.save(prompt));
    }

    public void delete(Long id) {
        Prompt prompt = getEntity(id);
        promptRepository.delete(prompt);
    }

    public PromptResponseDTO addTag(Long id, String tagName) {
        Prompt prompt = getEntity(id);
        Tag tag = tagService.findOrCreate(tagName);
        prompt.getTags().add(tag);
        return toResponse(promptRepository.save(prompt));
    }

    public PromptResponseDTO removeTag(Long id, String tagName) {
        Prompt prompt = getEntity(id);
        prompt.setTags(prompt.getTags().stream()
                .filter(t -> !t.getName().equalsIgnoreCase(tagName))
                .collect(Collectors.toSet()));
        return toResponse(promptRepository.save(prompt));
    }

    public Prompt getEntity(Long id) {
        return promptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prompt não encontrado: " + id));
    }

    private PromptResponseDTO toResponse(Prompt p) {
        String latest = p.getVersions() == null ? null : p.getVersions().stream()
                .map(v -> v.getVersion())
                .max(Comparator.comparing(PromptService::semverKey))
                .orElse(null);

        Set<String> tagNames = p.getTags() == null ? new HashSet<>() :
                p.getTags().stream().map(Tag::getName).collect(Collectors.toSet());

        return new PromptResponseDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getCategory(),
                tagNames,
                latest,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    public static long semverKey(String version) {
        try {
            String[] parts = version.split("\\.");
            return Long.parseLong(parts[0]) * 1_000_000L
                    + Long.parseLong(parts[1]) * 1_000L
                    + Long.parseLong(parts[2]);
        } catch (Exception e) {
            return 0L;
        }
    }
}
