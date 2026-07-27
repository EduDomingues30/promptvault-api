package com.eduardodomingues.promptvault.controller;

import com.eduardodomingues.promptvault.dto.PromptVersionRequestDTO;
import com.eduardodomingues.promptvault.dto.PromptVersionResponseDTO;
import com.eduardodomingues.promptvault.service.PromptVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prompts/{promptId}/versions")
@Tag(name = "Versões de Prompt", description = "Versionamento semântico de prompts")
public class PromptVersionController {

    private final PromptVersionService versionService;

    public PromptVersionController(PromptVersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping
    @Operation(summary = "Listar versões de um prompt")
    public List<PromptVersionResponseDTO> list(@PathVariable Long promptId) {
        return versionService.listByPrompt(promptId);
    }

    @GetMapping("/{version}")
    @Operation(summary = "Buscar versão específica")
    public PromptVersionResponseDTO get(@PathVariable Long promptId, @PathVariable String version) {
        return versionService.findByPromptAndVersion(promptId, version);
    }

    @PostMapping
    @Operation(summary = "Criar nova versão (semver: MAJOR.MINOR.PATCH)")
    public ResponseEntity<PromptVersionResponseDTO> create(@PathVariable Long promptId,
                                                          @Valid @RequestBody PromptVersionRequestDTO dto) {
        return ResponseEntity.status(201).body(versionService.create(promptId, dto));
    }

    @DeleteMapping("/{versionId}")
    @Operation(summary = "Remover versão")
    public ResponseEntity<Void> delete(@PathVariable Long promptId, @PathVariable Long versionId) {
        versionService.delete(versionId);
        return ResponseEntity.noContent().build();
    }
}
