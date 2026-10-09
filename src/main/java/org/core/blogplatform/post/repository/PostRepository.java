package org.core.blogplatform.post.repository;

import org.core.blogplatform.post.domain.Post;
import org.core.blogplatform.post.domain.PostStatus;
import org.core.blogplatform.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("""
            SELECT DISTINCT p FROM Post p
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.author
            LEFT JOIN FETCH p.category
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
              AND (:tagId IS NULL OR :tagId IN (SELECT t.id FROM p.tags t))
            """)
    List<Post> findByFilters(@Param("categoryId") UUID categoryId,
                             @Param("tagId") UUID tagId);

    @Query("""
            SELECT DISTINCT p FROM Post p
            LEFT JOIN FETCH p.tags
            LEFT JOIN FETCH p.category
            WHERE p.author = :author AND p.status = :status
            """)
    List<Post> findByAuthorAndStatus(@Param("author") User author,
                                     @Param("status") PostStatus status);
}