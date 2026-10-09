package org.core.blogplatform.post.dto.mapper;

import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.post.domain.Post;
import org.core.blogplatform.post.domain.PostStatus;
import org.core.blogplatform.post.dto.request.CreatePostRequest;
import org.core.blogplatform.post.dto.response.PostResponse;
import org.core.blogplatform.tag.domain.Tag;
import org.core.blogplatform.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post toEntity(CreatePostRequest request, User author, Category category, Set<Tag> tags) {
        return Post.builder()
                .title(request.title())
                .content(request.content())
                .status(request.status() != null ? request.status() : PostStatus.DRAFT)
                .readingTime(calculateReadingTime(request.content()))
                .author(author)
                .category(category)
                .tags(tags != null ? tags : Collections.emptySet())
                .build();
    }

    public PostResponse toResponse(Post post) {
        if (post == null) return null;

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getReadingTime(),
                post.getAuthor() != null
                        ? new PostResponse.AuthorSummary(post.getAuthor().getId(), post.getAuthor().getName())
                        : null,
                post.getCategory() != null
                        ? new PostResponse.CategorySummary(post.getCategory().getId(), post.getCategory().getName())
                        : null,
                post.getTags() == null
                        ? Collections.emptySet()
                        : post.getTags().stream()
                        .map(t -> new PostResponse.TagSummary(t.getId(), t.getName()))
                        .collect(Collectors.toSet()),
                post.getCreateAt(),
                post.getUpdateAt()
        );
    }


    //Approximate reading time: 200 words per minute.
    private int calculateReadingTime(String content) {
        if (content == null || content.isBlank()) return 0;
        int wordCount = content.trim().split("\\s+").length;
        return Math.max(1, (int) Math.ceil(wordCount / 200.0));
    }
}