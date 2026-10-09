package org.core.blogplatform.tag.service;

import lombok.RequiredArgsConstructor;
import org.core.blogplatform.tag.domain.Tag;
import org.core.blogplatform.tag.dto.mapper.TagMapper;
import org.core.blogplatform.tag.dto.response.TagResponse;
import org.core.blogplatform.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponse> getTags() {
        return tagRepository.findAllByPostCount().stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Override
    public List<TagResponse> createTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingNames = existingTags.stream().map(Tag::getName).collect(Collectors.toSet());

        //filtering new names
        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(tagMapper::toEntity)
                .toList();

        List<Tag> savedNewTags = tagRepository.saveAll(newTags);

        // Combine and map to responses
        List<Tag> allTags = new ArrayList<>(existingTags);
        allTags.addAll(savedNewTags);

        return allTags.stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteTag(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));

        if (tag.getPosts() != null && !tag.getPosts().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete tag with associated posts");
        }
        tagRepository.delete(tag);
    }

    @Override
    public TagResponse getTagById(UUID id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
    }

    @Override
    public List<TagResponse> getTagByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return tagRepository.findAllById(ids).stream()
                .map(tagMapper::toResponse)
                .toList();
    }
}
