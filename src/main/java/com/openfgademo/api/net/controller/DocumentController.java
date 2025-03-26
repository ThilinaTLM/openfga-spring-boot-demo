package com.openfgademo.api.net.controller;

import com.openfgademo.api.models.dto.document.DocumentDto;
import com.openfgademo.api.models.dto.document.DocumentFormDto;
import com.openfgademo.api.models.dto.document.DocumentQueryDto;
import com.openfgademo.api.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents/v1")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document management APIs")
public class DocumentController {

    private final DocumentService documentService;

    @Operation(summary = "Get document by ID", description = "Retrieves a document by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found",
                    content = @Content(schema = @Schema(implementation = DocumentDto.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @Operation(summary = "Search documents", description = "Retrieves documents based on query parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
    })
    @GetMapping("/search")
    public ResponseEntity<List<DocumentDto>> searchDocuments(
            @Valid DocumentQueryDto queryDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentService.getDocumentsByQuery(queryDto, page, size));
    }

    @Operation(summary = "Get documents by owner", description = "Retrieves documents owned by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
    })
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByOwner(
            @PathVariable UUID ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentService.getDocumentsByOwner(ownerId, page, size));
    }

    @Operation(summary = "Create document", description = "Creates a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody @Valid DocumentFormDto formDto) {
        DocumentDto createdDocument = documentService.createDocument(formDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    @Operation(summary = "Update document", description = "Updates an existing document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(
            @PathVariable UUID id,
            @RequestBody @Valid DocumentFormDto formDto) {
        return ResponseEntity.ok(documentService.updateDocument(id, formDto));
    }

    @Operation(summary = "Delete document", description = "Deletes a document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
} 