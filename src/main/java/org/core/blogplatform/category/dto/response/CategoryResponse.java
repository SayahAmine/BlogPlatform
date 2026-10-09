package org.core.blogplatform.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


public record CategoryResponse(
         UUID id,
         String name,
         Long postCount
) {}
