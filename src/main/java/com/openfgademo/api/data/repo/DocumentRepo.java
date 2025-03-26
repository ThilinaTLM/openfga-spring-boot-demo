package com.openfgademo.api.data.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openfgademo.api.data.entity.Document;

public interface DocumentRepo extends JpaRepository<Document, UUID> {
}