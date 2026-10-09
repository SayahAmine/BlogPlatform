package org.core.blogplatform.post.service;

import org.core.blogplatform.post.dto.request.CreatePostRequest;
import org.core.blogplatform.post.dto.request.UpdatePostRequest;
import org.core.blogplatform.post.dto.response.PostResponse;

import java.util.List;
import java.util.UUID;

public interface IPostService {

    PostResponse getPost(UUID id);
    List<PostResponse> getAllPosts(UUID categoryId, UUID tagId);
    List<PostResponse> getDraftPosts();
    PostResponse createPost(CreatePostRequest request);
    PostResponse updatePost(UUID id, UpdatePostRequest request);
    void deletePost(UUID id);
}
