package com.openfgademo.api.services;

import com.openfgademo.api.data.entity.Document;
import com.openfgademo.api.data.entity.User;
import com.openfgademo.api.data.repo.DocumentRepo;
import com.openfgademo.api.data.repo.UserRepo;
import com.openfgademo.api.models.common.AppException;
import com.openfgademo.api.models.dto.document.DocumentDto;
import com.openfgademo.api.models.dto.document.DocumentFormDto;
import com.openfgademo.api.models.dto.document.DocumentQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final UserRepo userRepo;
    private final OpenFgaService openFgaService;

    /**
     * Get a document by its ID
     *
     * @param id The document ID
     * @return Document DTO
     */
    public DocumentDto getDocumentById(UUID id) {
        Document document = documentRepo.findById(id)
                .orElseThrow(() -> new AppException("Document not found", HttpStatus.NOT_FOUND));

        return DocumentDto.fromEntity(document);
    }

    /**
     * Get documents based on query parameters
     *
     * @param queryDto Query parameters
     * @param page     Page number (0-based)
     * @param size     Page size
     * @return List of document DTOs
     */
    public List<DocumentDto> getDocumentsByQuery(DocumentQueryDto queryDto, int page, int size) {
        Specification<Document> spec = queryDto.toSpecs();

        Direction sortDirection = Sort.Direction.ASC;
        if (queryDto.getSortDirection() != null && queryDto.getSortDirection().equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        String sortBy = "updatedAt";
        if (queryDto.getSortBy() != null && !queryDto.getSortBy().isEmpty()) {
            sortBy = queryDto.getSortBy();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Document> results = documentRepo.findAll(spec, pageable);

        return results.getContent().stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get documents owned by a specific user
     *
     * @param ownerId Owner ID
     * @param page    Page number (0-based)
     * @param size    Page size
     * @return List of document DTOs
     */
    public List<DocumentDto> getDocumentsByOwner(UUID ownerId, int page, int size) {
        DocumentQueryDto queryDto = DocumentQueryDto.builder()
                .ownerId(ownerId)
                .build();

        return getDocumentsByQuery(queryDto, page, size);
    }

    /**
     * Create a new document
     *
     * @param formDto Document creation form
     * @return Created document DTO
     */
    @Transactional
    public DocumentDto createDocument(DocumentFormDto formDto) {
        if (formDto.getOwnerId() == null) {
            throw new AppException("Owner ID is required", HttpStatus.BAD_REQUEST);
        }

        User owner = userRepo.findById(formDto.getOwnerId())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Document document = formDto.toEntity(owner, UUID.randomUUID());
        Document savedDocument = documentRepo.save(document);

        // Assign owner relationship in OpenFGA
        openFgaService.setDocumentOwner("user:" + owner.getId().toString(), "document:" + savedDocument.getId().toString());

        return DocumentDto.fromEntity(savedDocument);
    }

    /**
     * Update an existing document
     *
     * @param id      Document ID
     * @param formDto Document update form
     * @return Updated document DTO
     */
    @Transactional
    public DocumentDto updateDocument(UUID id, DocumentFormDto formDto) {
        Document existingDocument = documentRepo.findById(id)
                .orElseThrow(() -> new AppException("Document not found", HttpStatus.NOT_FOUND));

        Document updatedDocument = formDto.updateEntity(existingDocument);
        Document savedDocument = documentRepo.save(updatedDocument);

        return DocumentDto.fromEntity(savedDocument);
    }

    /**
     * Delete a document by ID
     *
     * @param id Document ID
     */
    @Transactional
    public void deleteDocument(UUID id) {
        if (!documentRepo.existsById(id)) {
            throw new AppException("Document not found", HttpStatus.NOT_FOUND);
        }

        documentRepo.deleteById(id);
    }
} 