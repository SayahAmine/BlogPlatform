package org.core.blogplatform.post.service;

import lombok.RequiredArgsConstructor;
import org.core.blogplatform.category.domain.Category;
import org.core.blogplatform.category.repository.CategoryRepository;
import org.core.blogplatform.post.domain.Post;
import org.core.blogplatform.post.domain.PostStatus;
import org.core.blogplatform.post.dto.mapper.PostMapper;
import org.core.blogplatform.post.dto.request.CreatePostRequest;
import org.core.blogplatform.post.dto.request.UpdatePostRequest;
import org.core.blogplatform.post.dto.response.PostResponse;
import org.core.blogplatform.post.repository.PostRepository;
import org.core.blogplatform.tag.domain.Tag;
import org.core.blogplatform.tag.repository.TagRepository;
import org.core.blogplatform.user.domain.User;
import org.core.blogplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse getPost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        return postMapper.toResponse(post);
    }

    @Override
    public List<PostResponse> getAllPosts(UUID categoryId, UUID tagId) {
        return postRepository.findByFilters(categoryId, tagId)
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getDraftPosts() {
        User currentUser = getCurrentUser();
        return postRepository.findByAuthorAndStatus(currentUser, PostStatus.DRAFT)
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        User currentUser = getCurrentUser();

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.categoryId()));

        Set<Tag> tags = resolveTags(request.tagIds());

        Post post = postMapper.toEntity(request, currentUser, category, tags);
        Post saved = postRepository.save(post);

        return postMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PostResponse updatePost(UUID id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        // Authorization: only the author can edit their post
        User currentUser = getCurrentUser();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }

        // Apply partial updates (only non-null fields)
        if (request.title() != null) post.setTitle(request.title());
        if (request.status() != null) post.setStatus(request.status());
        if (request.content() != null) {
            post.setContent(request.content());
            post.setReadingTime(recalculateReadingTime(request.content()));
        }
        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            post.setCategory(category);
        }
        if (request.tagIds() != null) {
            post.setTags(resolveTags(request.tagIds()));
        }

        Post saved = postRepository.save(post);
        return postMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        User currentUser = getCurrentUser();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    //Helpers

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found in database"));
    }

    private Set<Tag> resolveTags(Set<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(tagRepository.findAllById(tagIds));
    }

    private int recalculateReadingTime(String content) {
        if (content == null || content.isBlank()) return 0;
        int wordCount = content.trim().split("\\s+").length;
        return Math.max(1, (int) Math.ceil(wordCount / 200.0));
    }
}