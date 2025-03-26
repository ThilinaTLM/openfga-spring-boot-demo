package com.openfgademo.api.models.dto.document;

import com.openfgademo.api.data.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Query parameters for searching documents")
public class DocumentQueryDto {

    @Schema(description = "Search by document title (partial match)", example = "Report")
    @Size(max = 255, message = "Title search term cannot exceed 255 characters")
    private String title;

    @Schema(description = "Search by document content (partial match)", example = "financial")
    private String content;

    @Schema(description = "Filter by owner ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID ownerId;

    @Schema(description = "Filter documents created after this date", example = "2023-01-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;

    @Schema(description = "Filter documents created before this date", example = "2023-12-31T23:59:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdBefore;

    @Schema(description = "Filter documents updated after this date", example = "2023-01-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAfter;

    @Schema(description = "Filter documents updated before this date", example = "2023-12-31T23:59:59")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedBefore;

    @Schema(description = "Sort direction", example = "asc", allowableValues = {"asc", "desc"})
    private String sortDirection;

    @Schema(description = "Sort field", example = "title", allowableValues = {"title", "createdAt", "updatedAt"})
    private String sortBy;

    /**
     * Converts this query DTO to a JPA Specification for Document entities.
     * Each non-null field in the DTO adds a filter condition to the specification.
     *
     * @return A Specification for filtering Document entities based on this DTO's criteria
     */
    public Specification<Document> toSpecs() {
        Specification<Document> spec = Specification.where(null);

        if (title != null && !title.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (content != null && !content.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
        }

        if (ownerId != null) {
            spec = spec.and((root, query, cb) -> {
                return cb.equal(root.get("owner").get("id"), ownerId);
            });
        }

        if (createdAfter != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));
        }

        if (createdBefore != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore));
        }

        if (updatedAfter != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("updatedAt"), updatedAfter));
        }

        if (updatedBefore != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("updatedAt"), updatedBefore));
        }

        return spec;
    }
} 