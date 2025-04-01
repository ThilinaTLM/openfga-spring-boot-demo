package com.openfgademo.api.services;

import com.openfgademo.api.models.openfga.FgaTuple;
import dev.openfga.sdk.api.client.OpenFgaClient;
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

}
