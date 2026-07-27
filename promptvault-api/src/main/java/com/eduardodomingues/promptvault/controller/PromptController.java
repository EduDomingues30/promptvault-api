package com.eduardodomingues.promptvault.controller;

import com.eduardodomingues.promptvault.dto.*;
import com.eduardodomingues.promptvault.service.PromptService;
import com.eduardodomingues.promptvault.service.PromptTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prompts")
@Tag(name = "Prompts", description = "Gerenciamento de prompts de IA")
public class PromptController {

    private final PromptService promptService;
    private final PromptTestService promptTestService;

    public PromptController(PromptService promptService, PromptTestService promptTestService) {
        this.promptService = promptService;
        this.promptTestService = promptTestService;
    }

    @GetMapping
    @Operation(summary = "Listar prompts paginados")
    public Page<PromptResponseDTO> list(Pageable pageable) {
        return promptService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar prompt por ID")
    public PromptResponseDTO get(@PathVariable Long id) {
        return promptService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Criar novo prompt")
    public ResponseEntity<PromptResponseDTO> create(@Valid @RequestBody PromptRequestDTO dto) {
        return ResponseEntity.status(201).body(promptService.create(dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar prompt parcialmente")
    public PromptResponseDTO update(@PathVariable Long id, @RequestBody PromptRequestDTO dto) {
        return promptService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover prompt")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        promptService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tags/{tagName}")
    @Operation(summary = "Adicionar tag ao prompt")
    public PromptResponseDTO addTag(@PathVariable Long id, @PathVariable String tagName) {
        return promptService.addTag(id, tagName);
    }

    @DeleteMapping("/{id}/tags/{tagName}")
    @Operation(summary = "Remover tag do prompt")
    public PromptResponseDTO removeTag(@PathVariable Long id, @PathVariable String tagName) {
        return promptService.removeTag(id, tagName);
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "Testar prompt via provider (Azure OpenAI ou Ollama - simulado)")
    public PromptTestResponseDTO testPrompt(@PathVariable Long id, @RequestBody PromptTestRequestDTO req) {
        return promptTestService.testPrompt(id, req);
    }
}
