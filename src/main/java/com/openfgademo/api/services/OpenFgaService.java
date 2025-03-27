package com.openfgademo.api.services;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientTupleKey;
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

            fgaClient.writeTuples(List.of(tuple)).get();
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

            fgaClient.deleteTuples(List.of(tuple)).get();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to remove relationship", e);
        }
    }
}
