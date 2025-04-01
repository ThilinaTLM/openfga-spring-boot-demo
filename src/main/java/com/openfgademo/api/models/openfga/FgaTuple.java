package com.openfgademo.api.models.openfga;

import dev.openfga.sdk.api.client.model.ClientTupleKey;

public record FgaTuple(FgaObject subject, FgaRelation relation, FgaObject object) {
    public FgaTuple {
        if (subject == null || relation == null || object == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
    }

    public static FgaTuple of(FgaObject subject, FgaRelation relation, FgaObject object) {
        return new FgaTuple(subject, relation, object);
    }

    public static FgaTuple of(String subject, FgaRelation relation, String object) {
        return new FgaTuple(FgaObject.of(subject), relation, FgaObject.of(object));
    }

    public ClientTupleKey toClientTupleKey() {
        return new ClientTupleKey()
                .user(subject.toString())
                .relation(relation.toString())
                ._object(object.toString());
    }
}
