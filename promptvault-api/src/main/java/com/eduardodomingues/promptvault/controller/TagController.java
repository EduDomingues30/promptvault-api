package com.eduardodomingues.promptvault.controller;

import com.eduardodomingues.promptvault.dto.TagDTO;
import com.eduardodomingues.promptvault.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@Tag(name = "Tags", description = "Tags de contexto para prompts")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as tags")
    public List<TagDTO> list() {
        return tagService.findAll();
    }
}
