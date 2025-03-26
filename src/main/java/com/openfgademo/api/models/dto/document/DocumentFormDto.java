package com.openfgademo.api.models.dto.document;

import com.openfgademo.api.data.entity.Document;
import com.openfgademo.api.data.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Form for creating or updating a document")
public class DocumentFormDto {

    @Schema(description = "Document title", example = "Quarterly Report", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(description = "Document content", example = "This document contains quarterly financial information...")
    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
    private String content;

    @Schema(description = "Owner ID (required for creation, ignored during update)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID ownerId;

    /**
     * Converts the DTO to a new Document entity
     *
     * @param owner The User entity that owns the document
     * @param id    The UUID to use for the document
     * @return Document entity with values from this DTO
     */
    public Document toEntity(User owner, UUID id) {
        LocalDateTime now = LocalDateTime.now();

        return Document.builder()
                .id(id)
                .title(this.title)
                .content(this.content)
                .owner(owner)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Updates an existing Document entity with values from this DTO
     *
     * @param document Document entity to update
     * @return Updated Document entity
     */
    public Document updateEntity(Document document) {
        document.setTitle(this.title);
        document.setContent(this.content);
        document.setUpdatedAt(LocalDateTime.now());
        return document;
    }
} 