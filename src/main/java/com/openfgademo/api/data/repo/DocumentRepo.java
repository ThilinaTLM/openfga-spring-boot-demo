package com.openfgademo.api.data.repo;

import com.openfgademo.api.data.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DocumentRepo extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {

    /**
     * Find documents by owner ID
     *
     * @param ownerId  The owner's ID
     * @param pageable Pagination information
     * @return Page of documents owned by the specified user
     */
    Page<Document> findByOwnerId(UUID ownerId, Pageable pageable);

    /**
     * Find documents by owner ID
     *
     * @param ownerId The owner's ID
     * @return List of documents owned by the specified user
     */
    List<Document> findByOwnerId(UUID ownerId);
}