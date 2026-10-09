package org.core.blogplatform.tag.repository;

import org.core.blogplatform.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Query("SELECT DISTINCT t FROM Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllByPostCount();

    List<Tag> findByNameIn(Set<String> tagNames);
}