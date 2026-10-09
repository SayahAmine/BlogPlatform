package org.core.blogplatform.tag.service;

import org.core.blogplatform.tag.dto.response.TagResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ITagService {

    List<TagResponse> getTags();
    List<TagResponse> createTags(Set<String> tagNames);
    void deleteTag(UUID id);
    TagResponse getTagById(UUID id);
    List<TagResponse> getTagByIds(Set<UUID> ids);
}
