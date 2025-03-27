package com.openfgademo.api.services;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientTupleKey;
import dev.openfga.sdk.api.client.model.ClientWriteResponse;
import dev.openfga.sdk.errors.FgaInvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OpenFgaService {

    private final OpenFgaClient fgaClient;

    /**
     * Create a relationship between a user and an object
     *
     * @param user     The user identifier (e.g., "user:123")
     * @param relation The relation (e.g., "owner", "editor", "viewer")
     * @param object   The object identifier (e.g., "document:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void createRelationship(String user, String relation, String object) {
        try {
            ClientTupleKey tuple = new ClientTupleKey()
                    .user(user)
                    .relation(relation)
                    ._object(object);

            ClientWriteResponse response = fgaClient.writeTuples(List.of(tuple)).get();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to create relationship", e);
        }
    }

    /**
     * Remove a relationship between a user and an object
     *
     * @param user     The user identifier (e.g., "user:123")
     * @param relation The relation (e.g., "owner", "editor", "viewer")
     * @param object   The object identifier (e.g., "document:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void removeRelationship(String user, String relation, String object) {
        try {
            ClientTupleKey tuple = new ClientTupleKey()
                    .user(user)
                    .relation(relation)
                    ._object(object);

            ClientWriteResponse response = fgaClient.deleteTuples(List.of(tuple)).get();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to remove relationship", e);
        }
    }

    /**
     * Add a user to a group
     *
     * @param userId  The user identifier (e.g., "user:123")
     * @param groupId The group identifier (e.g., "group:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void addUserToGroup(String userId, String groupId) {
        createRelationship(userId, "member", groupId);
    }

    /**
     * Remove a user from a group
     *
     * @param userId  The user identifier (e.g., "user:123")
     * @param groupId The group identifier (e.g., "group:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void removeUserFromGroup(String userId, String groupId) {
        removeRelationship(userId, "member", groupId);
    }

    /**
     * Makes a user the owner of a document
     *
     * @param userId     The user identifier (e.g., "user:123")
     * @param documentId The document identifier (e.g., "document:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void setDocumentOwner(String userId, String documentId) {
        createRelationship(userId, "owner", documentId);
    }

    /**
     * Add a user as a viewer of a document
     *
     * @param userId     The user identifier (e.g., "user:123")
     * @param documentId The document identifier (e.g., "document:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void addDocumentViewer(String userId, String documentId) {
        createRelationship(userId, "viewer", documentId);
    }

    /**
     * Add a user as an editor of a document
     *
     * @param userId     The user identifier (e.g., "user:123")
     * @param documentId The document identifier (e.g., "document:456")
     * @throws AuthorizationServiceException if an error occurs
     */
    public void addDocumentEditor(String userId, String documentId) {
        createRelationship(userId, "editor", documentId);
    }
}
