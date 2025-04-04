package com.openfgademo.api.services;

import com.openfgademo.api.models.openfga.FgaObject;
import com.openfgademo.api.models.openfga.FgaObjectType;
import com.openfgademo.api.models.openfga.FgaRelation;
import com.openfgademo.api.models.openfga.FgaTuple;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientListObjectsRequest;
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

    public void createRelationship(FgaTuple tuple) {
        try {
            fgaClient.writeTuples(List.of(tuple.toClientTupleKey())).get();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to create relationship", e);
        }
    }

    public List<String> getObjects(FgaObject user, FgaRelation relation, FgaObjectType type) {
        try {
            ClientListObjectsRequest request = new ClientListObjectsRequest()
                    .user(user.toString())
                    .relation(relation.toString())
                    .type(type.toString());
            var response = fgaClient.listObjects(request).get();
            return response.getObjects();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to get objects", e);
        }
    }

    public void deleteRelationship(FgaTuple tuple) {
        try {
            fgaClient.deleteTuples(List.of(tuple.toClientTupleKey())).get();
        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            throw new AuthorizationServiceException("Failed to delete relationship", e);
        }
    }
}
