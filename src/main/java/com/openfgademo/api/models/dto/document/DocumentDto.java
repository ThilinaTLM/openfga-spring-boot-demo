package com.openfgademo.api.models.dto.document;

import com.openfgademo.api.data.entity.Document;
import com.openfgademo.api.models.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Document information")
public class DocumentDto {

    @Schema(description = "Unique identifier of the document", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Document title", example = "Quarterly Report")
    private String title;

    @Schema(description = "Document content", example = "This document contains quarterly financial information...")
    private String content;

    @Schema(description = "Document owner")
    private UserDto owner;

    @Schema(description = "Timestamp when document was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when document was last updated")
    private LocalDateTime updatedAt;

    /**
     * Converts a Document entity to a DocumentDto
     *
     * @param document The document entity to convert
     * @return A DocumentDto populated with data from the entity
     */
    public static DocumentDto fromEntity(Document document) {
        if (document == null) {
            return null;
        }

        return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .owner(UserDto.fromEntity(document.getOwner()))
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
} 