package com.eduardodomingues.promptvault.service;

import com.eduardodomingues.promptvault.dto.TagDTO;
import com.eduardodomingues.promptvault.model.Tag;
import com.eduardodomingues.promptvault.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findOrCreate(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
    }

    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(t -> new TagDTO(t.getId(), t.getName()))
                .toList();
    }
}
