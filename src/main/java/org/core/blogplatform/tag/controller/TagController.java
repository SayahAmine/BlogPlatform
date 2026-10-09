package org.core.blogplatform.tag.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.core.blogplatform.tag.dto.request.TagRequest;
import org.core.blogplatform.tag.dto.response.TagResponse;
import org.core.blogplatform.tag.service.ITagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags(){
        return ResponseEntity.ok(tagService.getTags());
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@Valid  @RequestBody TagRequest tagRequest){
        List<TagResponse> createdTags = tagService.createTags(tagRequest.names());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTags);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable UUID id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<TagResponse>> getTagsByIds(
            @RequestParam(required = false) Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(tagService.getTagByIds(ids));
    }
}
