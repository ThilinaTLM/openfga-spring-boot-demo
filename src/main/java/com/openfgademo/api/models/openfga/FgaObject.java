package com.openfgademo.api.models.openfga;

public record FgaObject(FgaObjectType type, String id, FgaRelation relation) {
    public FgaObject {
        if (id == null) {
            throw new IllegalArgumentException("Id is required");
        }
    }

    public static FgaObject of(String id) {
        return new FgaObject(FgaObjectType.USER, id, null);
    }

    public static FgaObject of(FgaObjectType type, String id) {
        return new FgaObject(type, id, null);
    }

    public static FgaObject of(FgaObjectType type, String id, FgaRelation relation) {
        return new FgaObject(type, id, relation);
    }

    public static FgaObject of(FgaGroup group) {
        return new FgaObject(FgaObjectType.GROUP, group.toString(), null);
    }

    public static FgaObject of(FgaGroup group, FgaRelation relation) {
        return new FgaObject(FgaObjectType.GROUP, group.toString(), relation);
    }

    private String toStringWithoutRelation() {
        return type.toString() + ":" + id;
    }

    private String toValue() {
        return relation == null
                ? toStringWithoutRelation()
                : toStringWithoutRelation() + "#" + relation;
    }

    @Override
    public String toString() {
        return toValue();
    }
}
